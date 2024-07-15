package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.api.dto.response.TermsDetailResponse;
import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.dto.terms.UsedTermsInfo;
import com.juu.juulabel.domain.entity.terms.QTerms;
import com.juu.juulabel.domain.entity.terms.Terms;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TermsQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QTerms terms = QTerms.terms;

    public List<Terms> getAllByIsUsed() {
        return jpaQueryFactory
            .selectFrom(terms)
            .where(
                isUsed(terms),
                isNotDeleted(terms)
            )
            .fetch();
    }

    public List<UsedTermsInfo> getAllUsedTerms() {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    UsedTermsInfo.class,
                    terms.id,
                    terms.title,
                    terms.type,
                    terms.isRequired
                ))
            .from(terms)
            .where(
                isUsed(terms),
                isNotDeleted(terms)
            )
            .fetch();
    }

    public TermsDetailResponse getTermsById(Long termsId) {
        TermsDetailResponse termsDetailResponse = jpaQueryFactory
            .select(
                Projections.constructor(
                    TermsDetailResponse.class,
                    terms.id,
                    terms.title,
                    terms.content,
                    terms.type,
                    terms.isRequired
                ))
            .from(terms)
            .where(
                eqId(terms, termsId),
                isUsed(terms),
                isNotDeleted(terms)
            )
            .fetchOne();

        return Optional.ofNullable(termsDetailResponse)
            .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_TERMS));
    }

    private BooleanExpression eqId(QTerms terms, Long termsId) {
        return terms.id.eq(termsId);
    }

    private BooleanExpression isUsed(QTerms terms) {
        return terms.isUsed.isTrue();
    }

    private BooleanExpression isNotDeleted(QTerms terms) {
        return terms.deletedAt.isNull();
    }

}