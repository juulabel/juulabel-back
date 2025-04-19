package com.juu.juulabel.dailylife.repository.jpa;

import com.juu.juulabel.dailylife.domain.DailyLife;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyLifeJpaRepository extends JpaRepository<DailyLife, Long> {
    Optional<DailyLife> findByIdAndDeletedAtIsNull(Long id);
}
