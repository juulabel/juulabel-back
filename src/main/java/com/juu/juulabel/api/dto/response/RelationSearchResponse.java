package com.juu.juulabel.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.management.relation.Relation;
import java.util.List;

@Schema(description = "연관 검색어 리스트 조회 응답")
public record RelationSearchResponse(
        List<String> relationSearch
) {
}
