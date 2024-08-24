package com.juu.juulabel.api.service.s3;

import com.juu.juulabel.api.dto.response.ImageUploadResponse;
import com.juu.juulabel.domain.dto.s3.UploadImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Uploader s3Uploader;

    public static final String CATEGORY_MEMBER = "member";
    public static final String CATEGORY_DAILY_LIFE = "daily-life";

    public ImageUploadResponse uploadMemberImage(MultipartFile image) {
        UploadImageInfo uploadImageInfo = s3Uploader.uploadMultipartFileToBucket(CATEGORY_MEMBER, image);
        return new ImageUploadResponse(uploadImageInfo.ImageUrl());
    }

    public UploadImageInfo uploadDailyLifeImage(MultipartFile image) {
        return s3Uploader.uploadMultipartFileToBucket(CATEGORY_DAILY_LIFE, image);
    }

    public UploadImageInfo uploadMemberProfileImage(MultipartFile image) {
        return s3Uploader.uploadMultipartFileToBucket(CATEGORY_MEMBER, image);
    }

}