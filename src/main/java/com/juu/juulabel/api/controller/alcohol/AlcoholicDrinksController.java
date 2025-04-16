package com.juu.juulabel.api.controller.alcohol;


import com.juu.juulabel.api.dto.request.CategorySearchAlcoholRequest;
import com.juu.juulabel.api.dto.response.AlcoholicCategoryResponse;
import com.juu.juulabel.api.dto.response.AlcoholicDrinksDetailResponse;
import com.juu.juulabel.api.dto.response.BreweryDetailResponse;
import com.juu.juulabel.api.dto.response.RelationSearchResponse;
import com.juu.juulabel.api.service.alcohol.AlcoholicDrinksService;
import com.juu.juulabel.api.service.alcohol.BreweryService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "전통주 API",
        description = "전통주 관련 API"
)
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
//            @Parameter(hidden = true)
//            @LoginMember Member loginMember,
            @PathVariable Long alcoholicDrinksId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, alcoholicDrinksService.loadAlcoholicDrinks(alcoholicDrinksId));
    }

    @Operation(
            summary = "양조장 상세 조회",
            description = "")
    @GetMapping("/brewery/{breweryId}")
    public ResponseEntity<CommonResponse<BreweryDetailResponse>> loadBrewery(
            @PathVariable Long breweryId
    ) {
        return CommonResponse.success(SuccessCode.SUCCESS, breweryService.loadBreweryDetail(breweryId));
    }

    @Operation(
            summary = "연관 검색어 조회"
    )
    @GetMapping("/related-search")
    public ResponseEntity<CommonResponse<RelationSearchResponse>> loadRelatedSearch(
            @RequestParam String keyword
    ){
        return CommonResponse.success(SuccessCode.SUCCESS, alcoholicDrinksService.loadRelatedSearch(keyword));
    }

}
