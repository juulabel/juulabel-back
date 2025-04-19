package com.juu.juulabel.dailylife.repository.query;

import com.juu.juulabel.dailylife.domain.DailyLifeImage;
import com.juu.juulabel.dailylife.domain.QDailyLife;
import com.juu.juulabel.dailylife.domain.QDailyLifeImage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DailyLifeImageQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QDailyLifeImage dailyLifeImage = QDailyLifeImage.dailyLifeImage;

    public List<String> getImageUrlList(Long dailyLifeId) {
        return jpaQueryFactory
            .select(dailyLifeImage.imagePath)
            .from(dailyLifeImage)
            .where(
                eqId(dailyLifeImage.dailyLife, dailyLifeId),
                isNotDeleted(dailyLifeImage)
            )
            .fetch();
    }

    public List<DailyLifeImage> getImageList(Long dailyLifeId) {
        return jpaQueryFactory
            .selectFrom(dailyLifeImage)
            .where(
                eqId(dailyLifeImage.dailyLife, dailyLifeId),
                isNotDeleted(dailyLifeImage)
            )
            .fetch();
    }

    private BooleanExpression isNotDeleted(QDailyLifeImage dailyLifeImage) {
        return dailyLifeImage.deletedAt.isNull();
    }

    private BooleanExpression eqId(QDailyLife dailyLife, Long dailyLifeId) {
        return dailyLife.id.eq(dailyLifeId);
    }

}
