package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.*;
import com.juu.juulabel.domain.dto.tastingnote.LikeTopTastingNoteSummary;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSensorSummary;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.enums.sort.SortType;
import com.juu.juulabel.domain.repository.query.AlcoholDrinksDetailQueryRepository;
import com.juu.juulabel.domain.repository.query.AlcoholDrinksTypeQueryRepository;
import com.juu.juulabel.domain.repository.query.AlcoholicDrinksQueryRepository;
import com.juu.juulabel.domain.repository.query.BreweryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Reader
@RequiredArgsConstructor
public class AlcoholicDrinksReader {

    private final AlcoholicDrinksQueryRepository alcoholicDrinksQueryRepository;
    private final AlcoholDrinksDetailQueryRepository alcoholDrinksDetailQueryRepository;
    private final AlcoholDrinksTypeQueryRepository alcoholDrinksTypeQueryRepository;
    private final BreweryQueryRepository breweryQueryRepository;


    public AlcoholicDrinks findById(Long id) {
        return alcoholicDrinksQueryRepository.findById(id);
    }

    public AlcoholicDrinks getByIdOrElseNull(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }

        return alcoholicDrinksQueryRepository.findById(id);
    }

    public AlcoholicDrinks getById(Long id) {
        return Optional.ofNullable(findById(id))
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_ALCOHOLIC_DRINKS_TYPE));
    }

    public Slice<AlcoholicDrinksSummary> getAllMyAlcoholicDrinks(Member member, Long lastAlcoholicDrinksId, int pageSize) {
        return alcoholicDrinksQueryRepository.getAllMyAlcoholicDrinks(member, lastAlcoholicDrinksId, pageSize);
    }

    public AlcoholicDrinksDetailInfo getAlcoholDrinksDetailById(Long alcoholDrinksId) {
        return alcoholDrinksDetailQueryRepository.findAlcoholDrinksDetailById(alcoholDrinksId);
    }

    public List<IngredientSummary> getAlcoholDrinksIngredients(Long alcoholDrinksId) {
        return alcoholDrinksDetailQueryRepository.getIngredientSummary(alcoholDrinksId);
    }

    public TastingNoteSensorSummary getTastingNoteSensor(Long alcoholDrinksId) {
        Long mostLikedTastingNoteId = alcoholDrinksDetailQueryRepository.findMostLikedTastingNoteId(alcoholDrinksId);

        // 가장 좋아요가 많은 시음노트가 없다면, 필드 모두 null
        if (mostLikedTastingNoteId == null) {
            return new TastingNoteSensorSummary(
                    null,   // tastingNoteId
                    null,   // rgb
                    null,   // scent
                    null,   // flavor
                    null    // sensory
            );
        }
        return alcoholDrinksDetailQueryRepository.getTastingNoteSensor(mostLikedTastingNoteId);
    }

    public List<LikeTopTastingNoteSummary> getTastingNote(Long alcoholDrinksId) {
        return alcoholDrinksDetailQueryRepository.getTastingNoteList(alcoholDrinksId);
    }

    public Slice<AlcoholSearchSummary> getAlcoholicDrinksByType(Long alcoholTypeId, String lastAlcoholicDrinksName,int pageSize, SortType sortType){
        return alcoholDrinksTypeQueryRepository.findByAlcoholType(alcoholTypeId,lastAlcoholicDrinksName,pageSize,sortType);
    }

    public Double getAverageRating(Long alcoholTypeId){
        return alcoholDrinksDetailQueryRepository.getAverageRating(alcoholTypeId);
    }

    public Long getTastingNoteCount(Long alcoholTypeId){
        return alcoholDrinksDetailQueryRepository.getTastingNoteCount(alcoholTypeId);
    }

    // 테스트임
    public Long getMostLikedTastingNoteId(Long alcoholDrinksId) {
        return alcoholDrinksDetailQueryRepository.findMostLikedTastingNoteId(alcoholDrinksId);
    }

    public long countByAlcoholType(Long alcoholTypeId){
        return alcoholDrinksTypeQueryRepository.countByAlcoholType(alcoholTypeId);
    }

    public List<AlcoholicBrewerySummary> getAllByBreweryId(Long breweryId) {
        return breweryQueryRepository.getBreweryDetailById(breweryId);
    }

    public List<String> getRelatedSearch(String keyword){
        return alcoholicDrinksQueryRepository.getRelatedSearchByKeyword(keyword);
    }
}
