package com.juu.juulabel.api.controller.alcohol;

import com.juu.juulabel.api.service.alcohol.TastingNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1/api/tasting-notes"})
@RequiredArgsConstructor
public class TastingNoteController {

    private final TastingNoteService tastingNoteService;

//    @Operation(
//            summary = "시음 노트 전통주 검색",
//            description = "전통주명 또는 양조장으로 전통주를 검색한다."
//    )
//    @Parameters(
//            @Parameter(name = "request", description = "전통주 검색 요청", required = true)
//    )
//    @GetMapping("/search")
//    public ResponseEntity<CommonResponse<AlcoholDrinksListResponse>> loadAlcoholDrinksList() {
//        return CommonResponse.success(SuccessCode.SUCCESS, tastingNoteService.loadAlcoholDrinksList());
//    }

}
