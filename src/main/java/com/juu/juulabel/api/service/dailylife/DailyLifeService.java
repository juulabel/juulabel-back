package com.juu.juulabel.api.service.dailylife;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.juu.juulabel.api.dto.request.CommentListRequest;
import com.juu.juulabel.api.dto.request.DailyLifeListRequest;
import com.juu.juulabel.api.dto.request.ReplyListRequest;
import com.juu.juulabel.api.dto.request.UpdateCommentRequest;
import com.juu.juulabel.api.dto.request.UpdateDailyLifeRequest;
import com.juu.juulabel.api.dto.request.WriteDailyLifeCommentRequest;
import com.juu.juulabel.api.dto.request.WriteDailyLifeRequest;
import com.juu.juulabel.api.dto.response.DailyLifeCommentListResponse;
import com.juu.juulabel.api.dto.response.DailyLifeListResponse;
import com.juu.juulabel.api.dto.response.DailyLifeReplyListResponse;
import com.juu.juulabel.api.dto.response.DailyLifeResponse;
import com.juu.juulabel.api.dto.response.DeleteCommentResponse;
import com.juu.juulabel.api.dto.response.DeleteDailyLifeResponse;
import com.juu.juulabel.api.dto.response.UpdateCommentResponse;
import com.juu.juulabel.api.dto.response.UpdateDailyLifeResponse;
import com.juu.juulabel.api.dto.response.WriteDailyLifeCommentResponse;
import com.juu.juulabel.api.dto.response.WriteDailyLifeResponse;
import com.juu.juulabel.api.service.notification.NotificationService;
import com.juu.juulabel.api.service.s3.S3Service;
import com.juu.juulabel.common.constants.FileConstants;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.ImageInfo;
import com.juu.juulabel.domain.dto.comment.CommentSummary;
import com.juu.juulabel.domain.dto.comment.ReplySummary;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeDetailInfo;
import com.juu.juulabel.domain.dto.dailylife.DailyLifeSummary;
import com.juu.juulabel.domain.dto.member.MemberInfo;
import com.juu.juulabel.domain.dto.s3.UploadImageInfo;
import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import com.juu.juulabel.domain.entity.dailylife.DailyLifeComment;
import com.juu.juulabel.domain.entity.dailylife.DailyLifeImage;
import com.juu.juulabel.domain.entity.dailylife.like.DailyLifeCommentLike;
import com.juu.juulabel.domain.entity.dailylife.like.DailyLifeLike;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.reader.DailyLifeCommentLikeReader;
import com.juu.juulabel.domain.repository.reader.DailyLifeCommentReader;
import com.juu.juulabel.domain.repository.reader.DailyLifeImageReader;
import com.juu.juulabel.domain.repository.reader.DailyLifeLikeReader;
import com.juu.juulabel.domain.repository.reader.DailyLifeReader;
import com.juu.juulabel.domain.repository.writer.DailyLifeCommentLikeWriter;
import com.juu.juulabel.domain.repository.writer.DailyLifeCommentWriter;
import com.juu.juulabel.domain.repository.writer.DailyLifeImageWriter;
import com.juu.juulabel.domain.repository.writer.DailyLifeLikeWriter;
import com.juu.juulabel.domain.repository.writer.DailyLifeWriter;

import lombok.RequiredArgsConstructor;

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
	private final NotificationService notificationService;
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
		String notificationRelatedUrl = getRelatedUrl(dailyLifeId);
		boolean isNotSameUser = !dailyLife.getMember().isSameUser(member);

		// 좋아요가 등록되어 있다면 삭제, 등록되어 있지 않다면 등록
		return dailyLifeLike
			.map(like -> {
				dailyLifeLikeWriter.delete(like);

				if (isNotSameUser) {
					notificationService.deletePostLikeNotification(dailyLife.getMember(), member,
						notificationRelatedUrl);
				}
				return false;
			})
			.orElseGet(() -> {
				dailyLifeLikeWriter.store(member, dailyLife);

				if (isNotSameUser) {
					notificationService.sendPostLikeNotification(dailyLife.getMember(), member, notificationRelatedUrl);
				}
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

		String notificationRelatedUrl = getRelatedUrl(dailyLifeId);
		String notificationMessage;
		if (Objects.isNull(request.parentCommentId()) && (!dailyLife.getMember().isSameUser(member))) {
			notificationMessage = member.getNickname() + "님이 내 게시물에 댓글을 남겼어요.";
			notificationService.sendCommentNotification(dailyLife.getMember(), notificationRelatedUrl,
				notificationMessage, comment.getId(), member.getProfileImage());
		} else if (!comment.getMember().isSameUser(member)) {
			notificationMessage = member.getNickname() + "님이 내 댓글에 답글을 남겼어요.";
			notificationService.sendCommentNotification(comment.getMember(), notificationRelatedUrl,
				notificationMessage, comment.getId(), member.getProfileImage());
		}

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
			dailyLifeCommentReader.getAllByDailyLifeId(member, dailyLife.getId(), request.lastCommentId(),
				request.pageSize());

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
			dailyLifeCommentReader.getAllRepliesByParentId(member, dailyLife.getId(), dailyLifeCommentId,
				request.lastReplyId(), request.pageSize());

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
	public DeleteCommentResponse deleteComment(
		final Member member,
		final Long dailyLifeId,
		final Long commentId
	) {
		DailyLife dailyLife = getDailyLife(dailyLifeId);
		DailyLifeComment comment = getComment(commentId);
		validateCommentWriter(member, comment);
		comment.delete();

		String notificationRelatedUrl = getRelatedUrl(dailyLifeId);
		String notificationMessage;
		if (Objects.isNull(comment.getParent())) {
			notificationMessage = member.getNickname() + "님이 내 게시물에 댓글을 남겼어요.";
			notificationService.deleteCommentNotification(dailyLife.getMember(), notificationRelatedUrl,
				notificationMessage, commentId);
		} else {
			notificationMessage = member.getNickname() + "님이 내 댓글에 답글을 남겼어요.";
			notificationService.deleteCommentNotification(comment.getMember(), notificationRelatedUrl,
				notificationMessage, commentId);
		}

		return new DeleteCommentResponse(comment.getId());
	}

	@Transactional
	public boolean toggleCommentLike(final Member member, final Long dailyLifeId, final Long commentId) {
		getDailyLife(dailyLifeId);
		final DailyLifeComment comment = getComment(commentId);

		Optional<DailyLifeCommentLike> dailyLifeCommentLike =
			dailyLifeCommentLikeReader.findByMemberAndDailyLifeComment(member, comment);

		String notificationRelatedUrl = getRelatedUrl(dailyLifeId);
		String notificationMessage;
		if (Objects.isNull(comment.getParent())) {
			notificationMessage = member.getNickname() + "님이 내 댓글에 좋아요를 눌렀어요.";
		} else {
			notificationMessage = member.getNickname() + "님이 내 답글에 좋아요를 눌렀어요.";
		}
		boolean isNotSameUser = !comment.getMember().isSameUser(member);

		// 좋아요가 등록되어 있다면 삭제, 등록되어 있지 않다면 등록
		return dailyLifeCommentLike
			.map(like -> {
				dailyLifeCommentLikeWriter.delete(like);

				if (isNotSameUser) {
					notificationService.deleteCommentLikeNotification(comment.getMember(), notificationRelatedUrl,
						notificationMessage);
				}
				return false;
			})
			.orElseGet(() -> {
				dailyLifeCommentLikeWriter.store(member, comment);

				if (isNotSameUser) {
					notificationService.sendCommentLikeNotification(comment.getMember(), notificationRelatedUrl,
						notificationMessage, member.getProfileImage());
				}
				return true;
			});
	}

	private static String getRelatedUrl(Long dailyLifeId) {
		return "/v1/api/daily-lives/" + dailyLifeId;
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
