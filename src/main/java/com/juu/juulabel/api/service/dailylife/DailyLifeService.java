package com.juu.juulabel.api.service.dailylife;

import com.juu.juulabel.api.dto.request.WriteDailyLifeRequest;
import com.juu.juulabel.api.dto.response.WriteDailyLifeResponse;
import com.juu.juulabel.api.service.s3.S3Service;
import com.juu.juulabel.common.constants.FileConstants;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.member.MemberInfo;
import com.juu.juulabel.domain.dto.s3.UploadImageInfo;
import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.reader.MemberReader;
import com.juu.juulabel.domain.repository.writer.DailyLifeImageWriter;
import com.juu.juulabel.domain.repository.writer.DailyLifeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyLifeService {

    private final MemberReader memberReader;
    private final DailyLifeWriter dailyLifeWriter;
    private final DailyLifeImageWriter dailyLifeImageWriter;
    private final S3Service s3Service;

    @Transactional
    public WriteDailyLifeResponse writeDailyLife(
        final Member loginMember,
        final WriteDailyLifeRequest request,
        final List<MultipartFile> files
    ) {
        final Member member = memberReader.getById(loginMember.getId());
        DailyLife dailyLife = dailyLifeWriter.store(member, request);

        List<String> imageUrlList = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            // TODO : 파일 크기 및 확장자 validate
            validateFileListSize(files);

            for (int i = 1; i <= files.size(); i++) {
                MultipartFile file = files.get(i - 1);
                UploadImageInfo uploadImageInfo = s3Service.uploadDailyLifeImage(file);
                imageUrlList.add(uploadImageInfo.ImageUrl());

                dailyLifeImageWriter.store(dailyLife, i, uploadImageInfo.ImageUrl());
            }
        }

        return new WriteDailyLifeResponse(
            dailyLife.getId(),
            new MemberInfo(member.getId(), member.getNickname(), member.getProfileImage()),
            dailyLife.getTitle(),
            dailyLife.getContent(),
            dailyLife.isPrivate(),
            imageUrlList.isEmpty() ? null : imageUrlList,
            imageUrlList.isEmpty() ? 0 : imageUrlList.size()
        );
    }

    private static void validateFileListSize(List<MultipartFile> nonEmptyFiles) {
        if (nonEmptyFiles.size() > FileConstants.FILE_MAX_SIZE_COUNT) {
            throw new InvalidParamException(ErrorCode.EXCEEDED_FILE_COUNT);
        }
    }


}
