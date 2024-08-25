package com.juu.juulabel.api.controller.alcohol;

import com.juu.juulabel.api.dto.response.AlcoholTypeListResponse;
import com.juu.juulabel.api.service.alcohol.AlcoholService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1/api/alcohols"})
@RequiredArgsConstructor
public class AlcoholController {

    private final AlcoholService alcoholService;

    @Operation(summary = "주종 조회")
    @GetMapping("/types")
    public ResponseEntity<CommonResponse<AlcoholTypeListResponse>> loadUsedAlcoholTypeList() {
        return CommonResponse.success(SuccessCode.SUCCESS, alcoholService.loadUsedAlcoholTypeList());
    }

}