package com.juu.juulabel.domain.repository.reader;

import com.juu.juulabel.domain.annotation.Reader;
import com.juu.juulabel.domain.entity.alcohol.Scent;
import com.juu.juulabel.domain.repository.jpa.ScentJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class ScentReader {

    private final ScentJpaRepository scentJpaRepository;

    public List<Scent> getAllByIds(List<Long> ids) {
        return scentJpaRepository.findAllById(ids);
    }
}
