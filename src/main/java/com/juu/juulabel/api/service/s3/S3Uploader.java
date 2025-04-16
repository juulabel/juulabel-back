package com.juu.juulabel.api.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.s3.UploadImageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloudfront.url}")
    private String cloudFrontUrl;

    public static final String DATE_FORMAT_YYYYMMDD = "yyyy/MM/dd";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;  // 10MB

    public UploadImageInfo uploadMultipartFileToBucket(String category, MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BaseException(ErrorCode.FILE_TOO_LARGE);
        }
        // TODO : WebP 파일인지 검증

        String filePath = getFilePath(category, file.getName());
        ObjectMetadata metadata = createMetadataFromFile(file);

        try {
            amazonS3.putObject(
                new PutObjectRequest(bucket, filePath, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            );
        } catch (Exception e) {
            throw new BaseException(ErrorCode.S3_UPLOADER_ERROR);
        }

        return new UploadImageInfo(getCloudFrontUrl(filePath));
    }

    public File convertMultipartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }

    private String getFilePath(String category, String fileName) {
        return category + "/" + createDatePath() + "/" + generateRandomFilePrefix() + fileName;
    }

    private String createDatePath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD));
    }

    private ObjectMetadata createMetadataFromFile(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }

    private String generateRandomFilePrefix() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    private String getCloudFrontUrl(String fileKey) {
        return cloudFrontUrl + "/" + fileKey;
    }

}
