package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.alcohol.AlcoholType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlcoholTypeRepository extends JpaRepository<AlcoholType, Long> {
}
