package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.alcohol.AlcoholType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlcoholTypeJpaRepository extends JpaRepository<AlcoholType, Long> {
    Optional<AlcoholType> findByName(String name);

    Optional<AlcoholType> findFirstByName(String alcoholTypeName);
}
