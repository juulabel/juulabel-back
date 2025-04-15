package com.juu.juulabel.alcohol.repository.jpa;

import com.juu.juulabel.alcohol.domain.AlcoholType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlcoholTypeJpaRepository extends JpaRepository<AlcoholType, Long> {
    Optional<AlcoholType> findByName(String name);

    Optional<AlcoholType> findFirstByName(String alcoholTypeName);
}
