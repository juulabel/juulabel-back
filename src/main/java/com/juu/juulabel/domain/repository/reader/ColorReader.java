package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.common.exception.InvalidParamException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.alcohol.Color;
import com.juu.juulabel.domain.repository.jpa.ColorJpaRepository;
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
