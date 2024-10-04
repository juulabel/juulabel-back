package com.juu.juulabel.api.service.alcohol;

import com.juu.juulabel.api.dto.request.CategorySearchAlcoholRequest;
import com.juu.juulabel.api.dto.response.AlcoholicCategoryResponse;
import com.juu.juulabel.api.dto.response.AlcoholicDrinksDetailResponse;
import com.juu.juulabel.domain.dto.alcohol.AlcoholSearchSummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksDetailInfo;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSensorSummary;
import com.juu.juulabel.domain.entity.tastingnote.QTastingNote;
import com.juu.juulabel.domain.entity.tastingnote.QTastingNoteLike;
import com.juu.juulabel.domain.repository.query.AlcoholDrinksDetailQueryRepository;
import com.juu.juulabel.domain.repository.reader.AlcoholicDrinksReader;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlcoholicDrinksService {

    private static final Logger logger = LoggerFactory.getLogger(TastingNoteService.class);

    private final AlcoholicDrinksReader alcoholDrinksReader;
    private final AlcoholDrinksDetailQueryRepository alcoholDrinksDetailQueryRepository; // test
    private final TastingNoteService tastingNoteService;


    // 전통주 상세보기
    @Transactional(readOnly = true)
    public AlcoholicDrinksDetailResponse loadAlcoholicDrinks(final long alcoholicId) {

       AlcoholicDrinksDetailInfo alcoholicDrinksDetailInfo = alcoholDrinksReader.getAlcoholDrinksDetailById(alcoholicId);

       Double averageRating = alcoholDrinksReader.getAverageRating(alcoholicId);

       TastingNoteSensorSummary tastingNoteSensorSummary = alcoholDrinksReader.getTastingNoteSensor(alcoholicId);

         return new AlcoholicDrinksDetailResponse(
                alcoholicDrinksDetailInfo,
                 averageRating,
                 tastingNoteSensorSummary
         );
    }





    // test
    @Transactional(readOnly = true)
    public Long loadAlcoholicDrinksDetail(Long alcoholicId) {

        // 가장 좋아요를 많이 받은 시음노트 ID 가져오기
        Long mostLikedTastingNoteId = alcoholDrinksDetailQueryRepository.findMostLikedTastingNoteId(alcoholicId);

        return mostLikedTastingNoteId;
    }

    //test
    @Transactional(readOnly = true)
    public String logTastingNoteLikesCount(Long alcoholDrinksId) {
        List<Tuple> results = alcoholDrinksDetailQueryRepository.findTastingNoteLikesCount(alcoholDrinksId);

        // 쿼리 결과 로그 출력
        for (Tuple result : results) {
            Long tastingNoteId = result.get(QTastingNote.tastingNote.id);
            Long likesCount = result.get(QTastingNoteLike.tastingNoteLike.id.count());
            logger.info("TastingNote ID: {}, Likes Count: {}", tastingNoteId, likesCount);
        }

        // 빈 리스트 또는 메시지 반환
        return "쿼리 결과가 로그에 출력되었습니다.";
    }

    @Transactional(readOnly = true)
    public void logMostLikedTastingNoteId(Long alcoholDrinksId) {
        Long mostLikedTastingNoteId = alcoholDrinksDetailQueryRepository.findMostLikedTastingNoteId(alcoholDrinksId);
        if (mostLikedTastingNoteId != null) {
            log.info("가장 좋아요를 많이 받은 시음노트 ID: {}", mostLikedTastingNoteId);
        } else {
            log.warn("해당 전통주 ID {}에 대한 시음노트가 존재하지 않습니다.", alcoholDrinksId);
        }
    }





    // 주종별 검색 시 정렬
    @Transactional(readOnly = true)
    public AlcoholicCategoryResponse loadAlcoholDrinksList(final CategorySearchAlcoholRequest request) {

        // arrayType 이 null 이면 기본 정렬 "name" == 가나다순
        String arrayType = request.arrayType() == null || request.arrayType().isBlank() ? "name" :request.arrayType();

        final Slice<AlcoholSearchSummary> alcoholicDrinks = alcoholDrinksReader.getAlcoholicDrinksByType(request.type(),request.pageSize(), arrayType);

        // 검색된 전체 갯수 가져오기
        long totalCount = alcoholDrinksReader.countByAlcoholType(request.type());

        return new AlcoholicCategoryResponse(
                alcoholicDrinks.isLast(),
                totalCount,
                alcoholicDrinks
        );
    }

}
