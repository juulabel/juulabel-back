package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.alcohol.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorJpaRepository extends JpaRepository<Color, Long> {
}
