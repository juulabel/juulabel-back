package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.mapping.TermsMemberMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsMemberMappingJpaRepository extends JpaRepository<TermsMemberMapping, Long> {
}