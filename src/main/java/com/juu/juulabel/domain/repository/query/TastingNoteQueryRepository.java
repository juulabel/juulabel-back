package com.juu.juulabel.domain.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TastingNoteQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

}
