package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyLifeJpaRepository extends JpaRepository<DailyLife, Long> {
    Optional<DailyLife> findByIdAndDeletedAtIsNull(Long id);
}
