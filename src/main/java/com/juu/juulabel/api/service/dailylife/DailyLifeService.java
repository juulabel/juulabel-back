package com.juu.juulabel.api.service.dailylife;

import com.juu.juulabel.api.dto.request.*;
import com.juu.juulabel.api.dto.response.*;
import com.juu.juulabel.api.service.s3.S3Service;
import com.juu.juulabel.common.constants.FileConstants;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.ImageInfo;
import com.juu.juulabel.domain.dto.comment.CommentSummary;
import com.juu.juulabel.domain.dto.comment.ReplySummary;
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
        final Member member,
        final WriteDailyLifeRequest request,
        final List<MultipartFile> files
    ) {
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
    public DailyLifeResponse loadDailyLife(final Member member, final Long dailyLifeId) {
        final DailyLifeDetailInfo dailyLifeDetailInfo = dailyLifeReader.getDailyLifeDetailById(dailyLifeId, member);
        final List<String> urlList = dailyLifeImageReader.getImageUrlList(dailyLifeId);

        return new DailyLifeResponse(
            dailyLifeDetailInfo,
            new ImageInfo(urlList, urlList.size())
        );
    }

    @Transactional(readOnly = true)
    public DailyLifeListResponse loadDailyLifeList(Member member, DailyLifeListRequest request) {
        final Slice<DailyLifeSummary> dailyLifeList =
            dailyLifeReader.getAllDailyLives(member, request.lastDailyLifeId(), request.pageSize());

        return new DailyLifeListResponse(dailyLifeList);
    }

    @Transactional
    public UpdateDailyLifeResponse updateDailyLife(
        final Member member,
        final Long dailyLifeId,
        final UpdateDailyLifeRequest request,
        final List<MultipartFile> files
    ) {
        DailyLife dailyLife = getDailyLife(dailyLifeId);
        validateDailyLifeWriter(member, dailyLife);

        updateIfNotBlank(request.title(), dailyLife::updateTitle);
        updateIfNotBlank(request.content(), dailyLife::updateContent);
        dailyLife.updateIsPrivate(request.isPrivate());

        final List<DailyLifeImage> dailyLifeImageList = dailyLifeImageReader.getImageList(dailyLife.getId());
        dailyLifeImageList.forEach(DailyLifeImage::delete);

        List<String> newImageUrlList = new ArrayList<>();
        storeImageList(files, newImageUrlList, dailyLife);

        return new UpdateDailyLifeResponse(dailyLife.getId());
    }

    @Transactional
    public DeleteDailyLifeResponse deleteDailyLife(final Member member, final Long dailyLifeId) {
        DailyLife dailyLife = getDailyLife(dailyLifeId);
        validateDailyLifeWriter(member, dailyLife);

        dailyLife.delete();
        return new DeleteDailyLifeResponse(dailyLife.getId());
    }

    @Transactional
    public boolean toggleDailyLifeLike(final Member member, final Long dailyLifeId) {
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
        final Member member,
        final WriteDailyLifeCommentRequest request,
        final Long dailyLifeId
    ) {
        final DailyLife dailyLife = getDailyLife(dailyLifeId);

        final DailyLifeComment dailyLifeComment = createCommentOrReply(request, member, dailyLife);
        final DailyLifeComment comment = dailyLifeCommentWriter.store(dailyLifeComment);

        return new WriteDailyLifeCommentResponse(
            comment.getContent(),
            dailyLife.getId(),
            new MemberInfo(member.getId(), member.getNickname(), member.getProfileImage()));
    }

    @Transactional(readOnly = true)
    public DailyLifeCommentListResponse loadCommentList(
        final Member member,
        final CommentListRequest request,
        final Long dailyLifeId
    ) {
        final DailyLife dailyLife = getDailyLife(dailyLifeId);

        final Slice<CommentSummary> commentList =
            dailyLifeCommentReader.getAllByDailyLifeId(member, dailyLife.getId(), request.lastCommentId(), request.pageSize());

        return new DailyLifeCommentListResponse(commentList);
    }

    @Transactional(readOnly = true)
    public DailyLifeReplyListResponse loadReplyList(
        final Member member,
        final ReplyListRequest request,
        final Long dailyLifeId,
        final Long dailyLifeCommentId
    ) {
        final DailyLife dailyLife = getDailyLife(dailyLifeId);

        final Slice<ReplySummary> replyList =
            dailyLifeCommentReader.getAllRepliesByParentId(member, dailyLife.getId(), dailyLifeCommentId, request.lastReplyId(), request.pageSize());

        return new DailyLifeReplyListResponse(replyList);
    }

    @Transactional
    public UpdateCommentResponse updateComment(
        final Member member,
        final UpdateCommentRequest request,
        final Long dailyLifeId,
        final Long commentId
    ) {
        getDailyLife(dailyLifeId);
        DailyLifeComment comment = getComment(commentId);

        validateCommentWriter(member, comment);

        comment.updateContent(request.content());

        return new UpdateCommentResponse(
            comment.getContent(),
            new MemberInfo(member.getId(), member.getNickname(), member.getProfileImage()));
    }

    @Transactional
    public DeleteDailyLifeCommentResponse deleteComment(
        final Member member,
        final Long dailyLifeId,
        final Long commentId
    ) {
        getDailyLife(dailyLifeId);
        DailyLifeComment comment = getComment(commentId);

        validateCommentWriter(member, comment);

        comment.delete();
        return new DeleteDailyLifeCommentResponse(comment.getId());
    }

    @Transactional
    public boolean toggleCommentLike(final Member member, final Long dailyLifeId, final Long commentId) {
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
