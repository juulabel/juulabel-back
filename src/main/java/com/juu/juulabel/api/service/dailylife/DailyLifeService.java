package com.juu.juulabel.api.service.dailylife;

import com.juu.juulabel.api.dto.request.*;
import com.juu.juulabel.api.dto.response.*;
import com.juu.juulabel.api.service.s3.S3Service;
import com.juu.juulabel.common.constants.FileConstants;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.dailylife.*;
import com.juu.juulabel.domain.dto.member.MemberInfo;
import com.juu.juulabel.domain.dto.s3.UploadImageInfo;
import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import com.juu.juulabel.domain.entity.dailylife.DailyLifeComment;
import com.juu.juulabel.domain.entity.dailylife.DailyLifeImage;
import com.juu.juulabel.domain.entity.dailylife.like.DailyLifeCommentLike;
import com.juu.juulabel.domain.entity.dailylife.like.DailyLifeLike;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.reader.*;
import com.juu.juulabel.domain.repository.writer.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class DailyLifeService {

    private final MemberReader memberReader;
    private final DailyLifeWriter dailyLifeWriter;
    private final DailyLifeReader dailyLifeReader;
    private final DailyLifeImageWriter dailyLifeImageWriter;
    private final DailyLifeImageReader dailyLifeImageReader;
    private final DailyLifeCommentWriter dailyLifeCommentWriter;
    private final DailyLifeCommentReader dailyLifeCommentReader;
    private final DailyLifeLikeWriter dailyLifeLikeWriter;
    private final DailyLifeLikeReader dailyLifeLikeReader;
    private final DailyLifeCommentLikeWriter dailyLifeCommentLikeWriter;
    private final DailyLifeCommentLikeReader dailyLifeCommentLikeReader;
    private final S3Service s3Service;

    @Transactional
    public WriteDailyLifeResponse writeDailyLife(
        final Member loginMember,
        final WriteDailyLifeRequest request,
        final List<MultipartFile> files
    ) {
        final Member member = getMember(loginMember);
        final DailyLife dailyLife = dailyLifeWriter.store(member, request);

        List<String> imageUrlList = new ArrayList<>();
        storeImageList(files, imageUrlList, dailyLife);

        return new WriteDailyLifeResponse(
            dailyLife.getTitle(),
            dailyLife.getContent(),
            dailyLife.getId(),
            new MemberInfo(member.getId(), member.getNickname(), member.getProfileImage()),
            imageUrlList.isEmpty() ? null : imageUrlList,
            imageUrlList.isEmpty() ? 0 : imageUrlList.size()
        );
    }

    @Transactional(readOnly = true)
    public LoadDailyLifeResponse loadDailyLife(final Member loginMember, final Long dailyLifeId) {
        final Member member = getMember(loginMember);
        final DailyLifeDetailInfo dailyLifeDetailInfo = dailyLifeReader.getDailyLifeDetailById(dailyLifeId, member);
        final List<String> urlList = dailyLifeImageReader.getImageUrlList(dailyLifeId);

        return new LoadDailyLifeResponse(
            dailyLifeDetailInfo,
            new DailyLifeImageInfo(urlList, urlList.size())
        );
    }

    @Transactional(readOnly = true)
    public LoadDailyLifeListResponse loadDailyLifeList(Member loginMember, LoadDailyLifeListRequest request) {
        final Member member = getMember(loginMember);
        final Slice<DailyLifeSummary> dailyLifeList =
            dailyLifeReader.getAllDailyLife(member, request.lastDailyLifeId(), request.pageSize());

        return new LoadDailyLifeListResponse(dailyLifeList);
    }

    @Transactional
    public updateDailyLifeResponse updateDailyLife(
        final Member loginMember,
        final Long dailyLifeId,
        final UpdateDailyLifeRequest request,
        final List<MultipartFile> files
    ) {
        final Member member = getMember(loginMember);
        DailyLife dailyLife = getDailyLife(dailyLifeId);

        validateDailyLifeWriter(member, dailyLife);

        updateIfNotBlank(request.title(), dailyLife::updateTitle);
        updateIfNotBlank(request.content(), dailyLife::updateContent);
        dailyLife.updateIsPrivate(request.isPrivate());

        final List<DailyLifeImage> dailyLifeImageList = dailyLifeImageReader.getImageList(dailyLife.getId());
        dailyLifeImageList.forEach(DailyLifeImage::delete);

        List<String> newImageUrlList = new ArrayList<>();
        storeImageList(files, newImageUrlList, dailyLife);

        return new updateDailyLifeResponse(dailyLife.getId());
    }

    @Transactional
    public deleteDailyLifeResponse deleteDailyLife(final Member loginMember, final Long dailyLifeId) {
        final Member member = getMember(loginMember);
        DailyLife dailyLife = getDailyLife(dailyLifeId);

        validateDailyLifeWriter(member, dailyLife);

        dailyLife.delete();
        return new deleteDailyLifeResponse(dailyLife.getId());
    }

    @Transactional
    public boolean toggleDailyLifeLike(final Member loginMember, final Long dailyLifeId) {
        final Member member = getMember(loginMember);
        final DailyLife dailyLife = getDailyLife(dailyLifeId);
        Optional<DailyLifeLike> dailyLifeLike = dailyLifeLikeReader.findByMemberAndDailyLife(member, dailyLife);

        // 좋아요가 등록되어 있다면 삭제, 등록되어 있지 않다면 등록
        return dailyLifeLike
            .map(like -> {
                dailyLifeLikeWriter.delete(like);
                return false;
            })
            .orElseGet(() -> {
                dailyLifeLikeWriter.store(member, dailyLife);
                return true;
            });
    }

    @Transactional
    public WriteDailyLifeCommentResponse writeComment(
        final Member loginMember,
        final WriteDailyLifeCommentRequest request,
        final Long dailyLifeId
    ) {
        final Member member = getMember(loginMember);
        final DailyLife dailyLife = getDailyLife(dailyLifeId);

        final DailyLifeComment dailyLifeComment = createCommentOrReply(request, member, dailyLife);
        final DailyLifeComment comment = dailyLifeCommentWriter.store(dailyLifeComment);

        return new WriteDailyLifeCommentResponse(
            comment.getContent(),
            dailyLife.getId(),
            new MemberInfo(member.getId(), member.getNickname(), member.getProfileImage()));
    }

    @Transactional(readOnly = true)
    public LoadDailyLifeCommentListResponse loadCommentList(
        final Member loginMember,
        final LoadDailyLifeCommentListRequest request,
        final Long dailyLifeId
    ) {
        final Member member = getMember(loginMember);
        final DailyLife dailyLife = getDailyLife(dailyLifeId);

        final Slice<DailyLifeCommentSummary> commentList =
            dailyLifeCommentReader.getAllByDailyLifeId(member, dailyLife.getId(), request.lastCommentId(), request.pageSize());

        return new LoadDailyLifeCommentListResponse(commentList);
    }

    @Transactional(readOnly = true)
    public LoadDailyLifeReplyListResponse loadReplyList(
        final Member loginMember,
        final LoadDailyLifeReplyListRequest request,
        final Long dailyLifeId,
        final Long dailyLifeCommentId
    ) {
        final Member member = getMember(loginMember);
        final DailyLife dailyLife = getDailyLife(dailyLifeId);

        final Slice<DailyLifeReplySummary> replyList =
            dailyLifeCommentReader.getAllRepliesByParentId(member, dailyLife.getId(), dailyLifeCommentId, request.lastReplyId(), request.pageSize());

        return new LoadDailyLifeReplyListResponse(replyList);
    }

    @Transactional
    public UpdateDailyLifeCommentResponse updateComment(
        final Member loginMember,
        final UpdateDailyLifeCommentRequest request,
        final Long dailyLifeId,
        final Long commentId
    ) {
        final Member member = getMember(loginMember);
        getDailyLife(dailyLifeId);
        DailyLifeComment comment = getComment(commentId);

        validateCommentWriter(member, comment);

        comment.updateContent(request.content());

        return new UpdateDailyLifeCommentResponse(
            comment.getContent(),
            new MemberInfo(member.getId(), member.getNickname(), member.getProfileImage()));
    }

    @Transactional
    public deleteDailyLifeCommentResponse deleteComment(
        final Member loginMember,
        final Long dailyLifeId,
        final Long commentId
    ) {
        final Member member = getMember(loginMember);
        getDailyLife(dailyLifeId);
        DailyLifeComment comment = getComment(commentId);

        validateCommentWriter(member, comment);

        comment.delete();
        return new deleteDailyLifeCommentResponse(comment.getId());
    }

    @Transactional
    public boolean toggleCommentLike(final Member loginMember, final Long dailyLifeId, final Long commentId) {
        final Member member = getMember(loginMember);
        getDailyLife(dailyLifeId);
        final DailyLifeComment comment = getComment(commentId);

        Optional<DailyLifeCommentLike> dailyLifeCommentLike =
            dailyLifeCommentLikeReader.findByMemberAndDailyLifeComment(member, comment);

        // 좋아요가 등록되어 있다면 삭제, 등록되어 있지 않다면 등록
        return dailyLifeCommentLike
            .map(like -> {
                dailyLifeCommentLikeWriter.delete(like);
                return false;
            })
            .orElseGet(() -> {
                dailyLifeCommentLikeWriter.store(member, comment);
                return true;
            });
    }

    private static void validateCommentWriter(final Member member, final DailyLifeComment comment) {
        if (!member.getId().equals(comment.getMember().getId())) {
            throw new InvalidParamException(ErrorCode.NOT_COMMENT_WRITER);
        }
    }

    private static void validateDailyLifeWriter(final Member member, final DailyLife dailyLife) {
        if (!member.getId().equals(dailyLife.getMember().getId())) {
            throw new InvalidParamException(ErrorCode.NOT_DAILY_LIFE_WRITER);
        }
    }

    private DailyLifeComment createCommentOrReply(
        final WriteDailyLifeCommentRequest request,
        final Member member,
        final DailyLife dailyLife
    ) {
        if (Objects.isNull(request.parentCommentId())) {
            return DailyLifeComment.createComment(member, dailyLife, request.content());
        } else {
            DailyLifeComment parentComment = dailyLifeCommentReader.getById(request.parentCommentId());
            return DailyLifeComment.createReply(member, dailyLife, request.content(), parentComment);
        }
    }

    private DailyLifeComment getComment(final Long commentId) {
        return dailyLifeCommentReader.getById(commentId);
    }

    private DailyLife getDailyLife(final Long dailyLifeId) {
        return dailyLifeReader.getById(dailyLifeId);
    }

    private void updateIfNotBlank(final String value, final Consumer<String> updater) {
        if (StringUtils.hasText(value)) {
            updater.accept(value);
        }
    }

    private Member getMember(final Member loginMember) {
        return memberReader.getById(loginMember.getId());
    }

    private void storeImageList(
        final List<MultipartFile> files,
        final List<String> newImageUrlList,
        final DailyLife dailyLife
    ) {
        if (!Objects.isNull(files) && !files.isEmpty()) {
            // TODO : 파일 크기 및 확장자 validate
            validateFileListSize(files);

            for (MultipartFile file : files) {
                UploadImageInfo uploadImageInfo = s3Service.uploadDailyLifeImage(file);
                newImageUrlList.add(uploadImageInfo.ImageUrl());
                dailyLifeImageWriter.store(dailyLife, newImageUrlList.size(), uploadImageInfo.ImageUrl());
            }
        }
    }

    private static void validateFileListSize(final List<MultipartFile> nonEmptyFiles) {
        if (nonEmptyFiles.size() > FileConstants.FILE_MAX_SIZE_COUNT) {
            throw new InvalidParamException(ErrorCode.EXCEEDED_FILE_COUNT);
        }
    }
}
