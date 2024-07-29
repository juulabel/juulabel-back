package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.dailylife.DailyLife;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyLifeJpaRepository extends JpaRepository<DailyLife, Long> {
}
