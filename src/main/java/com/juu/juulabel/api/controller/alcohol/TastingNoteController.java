package com.juu.juulabel.api.controller.alcohol;

import com.juu.juulabel.api.dto.request.SearchAlcoholDrinksListRequest;
import com.juu.juulabel.api.dto.response.AlcoholDrinksListResponse;
import com.juu.juulabel.api.dto.response.TastingNoteSensoryListResponse;
import com.juu.juulabel.api.service.alcohol.TastingNoteService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            summary = "시음 노트 작성을 위한 시각 및 촉각 정보 조회",
            description = "주종에 따른 시각 및 촉각 정보를 조회한다."
    )
    @GetMapping("/{alcoholTypeId}/color-sensory")
    public ResponseEntity<CommonResponse<TastingNoteSensoryListResponse>> loadTastingNoteColorSensoryList(
            @PathVariable Long alcoholTypeId) {
        return CommonResponse.success(SuccessCode.SUCCESS, tastingNoteService.loadTastingNoteColorSensoryList(alcoholTypeId));
    }

}
