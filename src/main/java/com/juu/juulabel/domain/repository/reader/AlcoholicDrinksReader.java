package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.AlcoholSearchSummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicBrewerySummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksDetailInfo;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import com.juu.juulabel.domain.dto.tastingnote.TastingNoteSensorSummary;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.member.Member;
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

    public TastingNoteSensorSummary getTastingNoteSensor(Long alcoholDrinksId) {
        Long mostLikedTastingNoteId = alcoholDrinksDetailQueryRepository.findMostLikedTastingNoteId(alcoholDrinksId);
        return alcoholDrinksDetailQueryRepository.getTastingNoteSensor(mostLikedTastingNoteId);
    }

    public Slice<AlcoholSearchSummary> getAlcoholicDrinksByType(Long alcoholTypeId, int pageSize, String arrayType){
        return alcoholDrinksTypeQueryRepository.findByAlcoholType(alcoholTypeId,pageSize,arrayType);
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
}
