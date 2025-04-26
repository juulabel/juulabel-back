package com.juu.juulabel.report;

import com.juu.juulabel.common.annotation.LoginMember;
import com.juu.juulabel.common.exception.code.SuccessCode;
import com.juu.juulabel.common.response.CommonResponse;
import com.juu.juulabel.member.domain.Member;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "신고 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/reports")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<CommonResponse<Object>> createReport(
            @Parameter(hidden = true) @LoginMember Member member,
            @Valid @RequestBody ReportCreateRequest request) {
        reportService.createReport(member.getId(), request);
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT);
    }
}
