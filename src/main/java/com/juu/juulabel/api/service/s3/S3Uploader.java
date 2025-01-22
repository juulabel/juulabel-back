package com.juu.juulabel.api.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.juu.juulabel.common.exception.BaseException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.s3.UploadImageInfo;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloudfront.url}")
    private String cloudFrontUrl;

    public static final String DATE_FORMAT_YYYYMMDD = "yyyy/MM/dd";

    // WebP 변환 및 이미지 업로드
    public UploadImageInfo uploadMultipartFileToBucket(String category, MultipartFile file) {
        String filePath = getFilePath(category);
        File convertedFile = convertToWebpWithResize(file, filePath);
        ObjectMetadata metadata = createMetadataFromFile(convertedFile);

        try (FileInputStream fileInputStream = new FileInputStream(convertedFile)) {
            amazonS3.putObject(
                new PutObjectRequest(bucket, filePath, fileInputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            );
        } catch (Exception e) {
            throw new BaseException(ErrorCode.S3_UPLOADER_ERROR);
        }

        return new UploadImageInfo(getCloudFrontUrl(filePath));
    }

    // WebP로 변환 + 리사이징 및 압축
    public File convertToWebpWithResize(MultipartFile file, String fileName) {
        try {
            File originalFile = convertMultipartToFile(file);
            File webpFile = new File(createDatePath() + "/" + fileName + ".webp");
            Files.createDirectories(webpFile.getParentFile().toPath());

            ImmutableImage.loader()
                .fromFile(originalFile)
                .max(1280, 1280)
                .output(WebpWriter.DEFAULT, webpFile);
            return webpFile;

        } catch (Exception e) {
            throw new BaseException(ErrorCode.S3_UPLOADER_ERROR);
        }
    }

    // MultipartFile을 File로 변환하는 메서드
    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }

    private String getFilePath(String category) {
        return category + "/" + createDatePath() + "/" + generateRandomFilePrefix() + ".webp";
    }

    private String createDatePath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDD));
    }

    private ObjectMetadata createMetadataFromFile(File file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/webp");
        metadata.setContentLength(file.length());
        return metadata;
    }

    private String generateRandomFilePrefix() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    private String getCloudFrontUrl(String fileKey) {
        return cloudFrontUrl + "/" + fileKey;  // CloudFront 도메인과 S3 경로 결합
    }

}
