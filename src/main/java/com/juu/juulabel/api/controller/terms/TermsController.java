package com.juu.juulabel.api.controller.terms;

import com.juu.juulabel.api.dto.response.LoadTermsListResponse;
import com.juu.juulabel.api.dto.response.TermsDetailResponse;
import com.juu.juulabel.api.service.terms.TermsService;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1/api/terms"})
@RequiredArgsConstructor
public class TermsController {

    private final TermsService termsService;

    @Operation(summary = "약관 목록 조회")
    @GetMapping
    public ResponseEntity<CommonResponse<LoadTermsListResponse>> loadUsedTermsList() {
        return CommonResponse.success(SuccessCode.SUCCESS, termsService.loadUsedTermsList());
    }

    @Operation(summary = "약관 상세 조회")
    @GetMapping("/{termsId}")
    public ResponseEntity<CommonResponse<TermsDetailResponse>> loadTermsDetail(@NotNull @PathVariable Long termsId) {
        return CommonResponse.success(SuccessCode.SUCCESS, termsService.loadTermsDetail(termsId));
    }

}