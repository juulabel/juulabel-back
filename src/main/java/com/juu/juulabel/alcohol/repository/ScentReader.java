package com.juu.juulabel.alcohol.repository;

import com.juu.juulabel.common.annotation.Reader;
import com.juu.juulabel.alcohol.domain.Scent;
import com.juu.juulabel.alcohol.repository.jpa.ScentJpaRepository;
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
