package com.juu.juulabel.domain.repository.writer;

import com.juu.juulabel.domain.annotation.Writer;
import com.juu.juulabel.domain.entity.mapping.TermsMemberMapping;
import com.juu.juulabel.domain.repository.jpa.TermsMemberMappingJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Writer
@RequiredArgsConstructor
public class TermsMemberMappingWriter {

    private final TermsMemberMappingJpaRepository termsMemberMappingJpaRepository;

    public List<TermsMemberMapping> storeAll(List<TermsMemberMapping> termsMemberMappingList) {
        return termsMemberMappingJpaRepository.saveAll(termsMemberMappingList);
    }

}