package com.juu.juulabel.api.controller.alcohol;


import com.juu.juulabel.api.annotation.LoginMember;
import com.juu.juulabel.api.dto.request.CategorySearchAlcoholRequest;
import com.juu.juulabel.api.dto.request.SearchAlcoholDrinksListRequest;
import com.juu.juulabel.api.dto.response.AlcoholDrinksListResponse;
import com.juu.juulabel.api.dto.response.AlcoholicCategoryResponse;
import com.juu.juulabel.api.dto.response.AlcoholicDrinksDetailResponse;
import com.juu.juulabel.api.service.alcohol.AlcoholicDrinksService;
import com.juu.juulabel.api.service.alcohol.TastingNoteService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.domain.entity.member.Member;
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
@RequestMapping(value = {"/v1/api/shared-space/category"})
@RequiredArgsConstructor
public class AlcoholCategorySearchController {

    private final TastingNoteService tastingNoteService;
    private final AlcoholicDrinksService alcoholicDrinksService;

    @Operation(
            summary = "전통주 주종별 검색",
            description = ""
    )
    @Parameters(
            @Parameter(name = "request", description = "주종별 검색 요청", required = true)
    )
    @GetMapping("/typeSearch")
    public ResponseEntity<CommonResponse<AlcoholicCategoryResponse>> searchAlcoholDrinksList(
            @Parameter(hidden = true)
            @LoginMember Member loginMember,
            @Valid CategorySearchAlcoholRequest request
    ) {
    return CommonResponse.success(SuccessCode.SUCCESS, tastingNoteService.searchAlcoholTypeList(loginMember,request));
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
}
