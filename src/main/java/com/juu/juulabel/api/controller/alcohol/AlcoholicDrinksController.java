package com.juu.juulabel.api.controller.alcohol;


import com.juu.juulabel.api.dto.request.CategorySearchAlcoholRequest;
import com.juu.juulabel.api.dto.response.AlcoholicCategoryResponse;
import com.juu.juulabel.api.dto.response.AlcoholicDrinksDetailResponse;
import com.juu.juulabel.api.dto.response.BreweryDetailResponse;
import com.juu.juulabel.api.service.alcohol.AlcoholicDrinksService;
import com.juu.juulabel.api.service.alcohol.BreweryService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.querydsl.core.Tuple;
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

import java.util.List;

@RestController
@RequestMapping(value = {"/v1/api/alcoholicDrinks"})
@RequiredArgsConstructor
public class AlcoholicDrinksController {

    private final AlcoholicDrinksService alcoholicDrinksService;
    private final BreweryService breweryService;


    // 주종별 검색 시 정렬 기능
    @Operation(
            summary = "전통주 주종별 검색"
    )
    @Parameters(
            @Parameter(name = "request", description = "주종별 검색 요청", required = true)
    )
    @GetMapping("/typeSearch")
    public ResponseEntity<CommonResponse<AlcoholicCategoryResponse>> loadAlcoholDrinksList(
            // @Parameter(hidden = true) @LoginMember Member loginMember,
            @Valid CategorySearchAlcoholRequest request
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, alcoholicDrinksService.loadAlcoholDrinksList(request));
    }


    @Operation(
            summary = "전통주 상세 조회",
            description = ""
    )
    @GetMapping("/{alcoholicDrinksId}")
    public ResponseEntity<CommonResponse<AlcoholicDrinksDetailResponse>> loadAlcoholDrinks(
            //@Parameter(hidden = true)
            //@LoginMember Member loginMember,
            @PathVariable Long alcoholicDrinksId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, alcoholicDrinksService.loadAlcoholicDrinks(alcoholicDrinksId));
    }

    // 좋아요 젤 많ㅎ이 받은 시음노트id
    @GetMapping("/{alcoholicDrinksId}/most-liked-tasting-note")
    public ResponseEntity<CommonResponse<Long>> getMostLikedTastingNoteId(
            @PathVariable Long alcoholicDrinksId
    ) {
        Long mostLikedTastingNoteId = alcoholicDrinksService.loadAlcoholicDrinksDetail(alcoholicDrinksId);

        return CommonResponse.success(SuccessCode.SUCCESS, mostLikedTastingNoteId);
    }

    // 전통주 id에 따른 시음노트별 좋아요 수 카운트
    @GetMapping("/testingNoteLikeLog/{alcoholDrinksId}")
    public ResponseEntity<String> logTastingNoteLikesCount(@PathVariable Long alcoholDrinksId) {
        String message = alcoholicDrinksService.logTastingNoteLikesCount(alcoholDrinksId);
        return ResponseEntity.ok(message);
    }

    // 전통주 id에 따른 좋아요수 젤 많은 시음노트 id 출력
    @GetMapping("/most-liked/{alcoholDrinksId}")
    public ResponseEntity<String> logMostLikedTastingNoteId(@PathVariable Long alcoholDrinksId) {
        alcoholicDrinksService.logMostLikedTastingNoteId(alcoholDrinksId);
        return ResponseEntity.ok("가장 좋아요를 많이 받은 시음노트 ID를 로그로 확인했습니다.");
    }

    @Operation(
            summary = "양조장 상세 조회",
            description = "")
    @GetMapping("/brewery/{breweryId}")
    public ResponseEntity<CommonResponse<BreweryDetailResponse>> loadBrewery(
            @PathVariable Long breweryId
    ){
        return CommonResponse.success(SuccessCode.SUCCESS,breweryService.loadBreweryDetail(breweryId));
    }

}
