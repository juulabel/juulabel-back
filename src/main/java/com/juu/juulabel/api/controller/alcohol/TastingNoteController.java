package com.juu.juulabel.api.controller.alcohol;

import com.juu.juulabel.api.annotation.LoginMember;
import com.juu.juulabel.api.dto.request.SearchAlcoholDrinksListRequest;
import com.juu.juulabel.api.dto.request.TastingNoteWriteRequest;
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

}
