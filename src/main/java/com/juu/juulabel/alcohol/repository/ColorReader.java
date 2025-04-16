package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.alcohol.domain.Color;
import com.juu.juulabel.alcohol.repository.jpa.ColorJpaRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class ColorReader {

    private final ColorJpaRepository colorJpaRepository;

    public Color getById(Long id) {
        return colorJpaRepository.findById(id)
                .orElseThrow(() -> new InvalidParamException(ErrorCode.NOT_FOUND_COLOR));
    }

}
