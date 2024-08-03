package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.dto.alcohol.AlcoholicDrinksSummary;
import com.juu.juulabel.domain.repository.query.TastingNoteQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

@Reader
@RequiredArgsConstructor
public class TastingNoteReader {

    private final TastingNoteQueryRepository tastingNoteQueryRepository;

    public Slice<AlcoholicDrinksSummary> getAllAlcoholicDrinks(final String search,
                                                               final String lastAlcoholicDrinksName,
                                                               final int pageSize) {
        return tastingNoteQueryRepository.findAllAlcoholicDrinks(search, lastAlcoholicDrinksName, pageSize);
    }

}
