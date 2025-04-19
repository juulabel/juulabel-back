package com.juu.juulabel.s3;

import com.juu.juulabel.common.dto.response.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Uploader s3Uploader;

    public static final String CATEGORY_MEMBER = "member";
    public static final String CATEGORY_DAILY_LIFE = "daily-life";
    public static final String CATEGORY_TASTING_NOTE = "tasting-note";

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

    public UploadImageInfo uploadTastingNoteImage(MultipartFile image) {
        return s3Uploader.uploadMultipartFileToBucket(CATEGORY_TASTING_NOTE, image);
    }
}