package com.juu.juulabel.api.service.s3;

import com.juu.juulabel.api.dto.response.ImageUploadResponse;
import com.juu.juulabel.domain.dto.s3.UploadImageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Uploader s3Uploader;

    public static final String CATEGORY_MEMBER = "member";

    public ImageUploadResponse uploadMemberImage(MultipartFile image) {
        UploadImageInfo uploadImageInfo = s3Uploader.uploadMultipartFileToBucket(CATEGORY_MEMBER, image);
        return new ImageUploadResponse(uploadImageInfo.ImageUrl());
    }

}