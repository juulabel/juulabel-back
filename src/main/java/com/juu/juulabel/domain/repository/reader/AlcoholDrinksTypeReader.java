package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.AlcoholSearchSummary;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.entity.member.Member;
import com.juu.juulabel.domain.repository.query.AlcoholDrinksTypeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

@Reader
@RequiredArgsConstructor
public class AlcoholDrinksTypeReader {

    private final AlcoholDrinksTypeQueryRepository alcoholDrinksTypeQueryRepository;


    public Slice<AlcoholSearchSummary> getAlcoholicDrinksByType(final Member member, Long alcoholTypeId, int pageSize){
        return alcoholDrinksTypeQueryRepository.findByAlcoholType(member,alcoholTypeId,pageSize);
    }

    public long countByAlcoholType(Long alcoholTypeId){
        return alcoholDrinksTypeQueryRepository.countByAlcoholType(alcoholTypeId);
    }
}
