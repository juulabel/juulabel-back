package com.juu.juulabel.api.controller.alcohol;

import com.juu.juulabel.api.annotation.LoginMember;
import com.juu.juulabel.api.dto.request.*;
import com.juu.juulabel.api.dto.response.*;
import com.juu.juulabel.api.service.alcohol.TastingNoteService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(
        name = "시음노트 API",
        description = "시음노트 관련 API"
)
@RestController
@RequestMapping(value = {"/v1/api/shared-space/tasting-notes"})
@RequiredArgsConstructor
public class TastingNoteController {

    private final TastingNoteService tastingNoteService;

    // TODO : 권한 설정 + security
    @Operation(
            summary = "시음 노트 전통주 검색",
            description = "전통주명 또는 양조장으로 전통주를 검색한다."
    )
    @Parameters(
            @Parameter(name = "request", description = "전통주 검색 요청", required = true)
    )
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<AlcoholDrinksListResponse>> searchAlcoholDrinksList(
            @Valid SearchAlcoholDrinksListRequest request
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, tastingNoteService.searchAlcoholDrinksList(request));
    }

    @Operation(
            summary = "시음 노트 작성을 위한 시각 정보 조회",
            description = "주종에 따른 시각 정보를 조회한다."
    )
    @GetMapping("/{alcoholTypeId}/colors")
    public ResponseEntity<CommonResponse<TastingNoteColorListResponse>> loadTastingNoteColorsList(
            @PathVariable Long alcoholTypeId) {
        return CommonResponse.success(SuccessCode.SUCCESS, tastingNoteService.loadTastingNoteColorsList(alcoholTypeId));
    }

    @Operation(
            summary = "시음 노트 작성을 위한 촉각 정보 조회",
            description = "주종에 따른 촉각 정보를 조회한다."
    )
    @GetMapping("/{alcoholTypeId}/sensory")
    public ResponseEntity<CommonResponse<TastingNoteSensoryListResponse>> loadTastingNoteSensoryList(
            @PathVariable Long alcoholTypeId) {
        return CommonResponse.success(SuccessCode.SUCCESS, tastingNoteService.loadTastingNoteSensoryList(alcoholTypeId));
    }

    @Operation(
            summary = "시음 노트 작성을 위한 후각 정보 조회",
            description = "주종에 따른 후각 정보를 조회한다."
    )
    @GetMapping("/{alcoholTypeId}/scent")
    public ResponseEntity<CommonResponse<TastingNoteScentListResponse>> loadTastingNoteScentList(
            @PathVariable Long alcoholTypeId) {
        return CommonResponse.success(SuccessCode.SUCCESS, tastingNoteService.loadTastingNoteScentList(alcoholTypeId));
    }

    @Operation(
            summary = "시음 노트 작성을 위한 미각 정보 조회",
            description = "미각 정보를 조회한다."
    )
    @GetMapping("/{alcoholTypeId}/flavor")
    public ResponseEntity<CommonResponse<TastingNoteFlavorListResponse>> loadTastingNoteFlavorList(
            @PathVariable Long alcoholTypeId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, tastingNoteService.loadTastingNoteFlavorList(alcoholTypeId));
    }

    @Operation(
            summary = "시음 노트 작성",
            description = "시음노트를 작성한다."
    )
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<TastingNoteWriteResponse>> write(
            @Parameter(hidden = true) @LoginMember Member loginMember,
            @Valid @RequestPart(value = "request") TastingNoteWriteRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, tastingNoteService.write(loginMember, request, files));
    }

    @Operation(
        summary = "시음노트 목록 조회",
        description = "전통주 시음노트 게시글 목록을 조회한다."
    )
    @Parameters(
        @Parameter(name = "request", description = "시음노트 게시글 목록 조회 요청", required = true)
    )
    @GetMapping
    public ResponseEntity<CommonResponse<TastingNoteListResponse>> loadTastingNoteList(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid TastingNoteListRequest request
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, tastingNoteService.loadTastingNoteList(loginMember, request));
    }

    @Operation(
        summary = "시음노트 상세 조회",
        description = "전통주 시음노트 게시글을 상세 조회한다."
    )
    @GetMapping("/{tastingNoteId}")
    public ResponseEntity<CommonResponse<TastingNoteResponse>> loadTastingNote(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long tastingNoteId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, tastingNoteService.loadTastingNote(loginMember, tastingNoteId));
    }

    @Operation(
        summary = "시음노트 수정",
        description = "전통주 시음노트 게시글을 수정한다."
    )
    @PutMapping(value = "/{tastingNoteId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<TastingNoteWriteResponse>> updateTastingNote(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid @RequestPart(value = "request") TastingNoteWriteRequest request,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @PathVariable Long tastingNoteId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE, tastingNoteService.updateTastingNote(loginMember, tastingNoteId, request, files));
    }

    @Operation(
        summary = "시음노트 삭제",
        description = "전통주 시음노트 게시글을 삭제한다."
    )
    @DeleteMapping("/{tastingNoteId}")
    public ResponseEntity<CommonResponse<DeleteTastingNoteResponse>> deleteTastingNote(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long tastingNoteId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_DELETE, tastingNoteService.deleteTastingNote(loginMember, tastingNoteId));
    }

    @Operation(
        summary = "시음노트 좋아요",
        description = "전통주 시음노트 게시글에 좋아요를 등록 및 취소한다."
    )
    @PostMapping("/{tastingNoteId}/likes")
    public ResponseEntity<CommonResponse<Void>> toggleTastingNoteLike(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable Long tastingNoteId
    ) {
        boolean isLiked = tastingNoteService.toggleTastingNoteLike(loginMember, tastingNoteId);
        return CommonResponse.success(isLiked ? SuccessCode.SUCCESS_INSERT : SuccessCode.SUCCESS_DELETE);
    }

    @Operation(
        summary = "시음노트 댓글 작성",
        description = "전통주 시음노트 게시글에 댓글을 작성한다."
    )
    @PostMapping("/{tastingNoteId}/comments")
    public ResponseEntity<CommonResponse<WriteTastingNoteCommentResponse>> writeComment(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid @RequestBody WriteTastingNoteCommentRequest request,
        @PathVariable Long tastingNoteId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, tastingNoteService.writeComment(loginMember, request, tastingNoteId));
    }

    @Operation(
        summary = "시음노트 댓글 목록 조회",
        description = "전통주 시음노트 게시글의 댓글 목록을 조회한다."
    )
    @Parameters(
        @Parameter(name = "request", description = "시음노트 게시글 댓글 목록 조회 요청", required = true)
    )
    @GetMapping("/{tastingNoteId}/comments")
    public ResponseEntity<CommonResponse<TastingNoteCommentListResponse>> loadCommentList(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Valid CommentListRequest request,
        @PathVariable Long tastingNoteId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, tastingNoteService.loadCommentList(loginMember, request, tastingNoteId));
    }

}
