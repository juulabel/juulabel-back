package com.juu.juulabel.domain.repository.query;

import com.juu.juulabel.domain.entity.tastingnote.QTastingNote;
import com.juu.juulabel.domain.entity.tastingnote.QTastingNoteImage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TastingNoteImageQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QTastingNoteImage tastingNoteImage = QTastingNoteImage.tastingNoteImage;


    public List<String> getImageUrlList(Long tastingNoteId) {
        return jpaQueryFactory
            .select(tastingNoteImage.imagePath)
            .from(tastingNoteImage)
            .where(
                eqId(tastingNoteImage.tastingNote, tastingNoteId),
                isNotDeleted(tastingNoteImage)
            )
            .fetch();
    }

    private BooleanExpression isNotDeleted(QTastingNoteImage tastingNoteImage) {
        return tastingNoteImage.deletedAt.isNull();
    }

    private BooleanExpression eqId(QTastingNote tastingNote, Long tastingNoteId) {
        return tastingNote.id.eq(tastingNoteId);
    }

}
