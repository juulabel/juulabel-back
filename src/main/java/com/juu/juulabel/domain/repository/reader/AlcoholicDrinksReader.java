package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import com.juu.juulabel.domain.repository.query.AlcoholicDrinksQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@Reader
@RequiredArgsConstructor
public class AlcoholicDrinksReader {

    private final AlcoholicDrinksQueryRepository alcoholicDrinksQueryRepository;

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

}
