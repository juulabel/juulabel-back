package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.dto.terms.UsedTermsInfo;
import com.juu.juulabel.domain.entity.terms.QTerms;
import com.juu.juulabel.domain.entity.terms.Terms;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                    terms.content,
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

    private BooleanExpression isUsed(QTerms terms) {
        return terms.isUsed.isTrue();
    }

    private BooleanExpression isNotDeleted(QTerms terms) {
        return terms.deletedAt.isNull();
    }

}