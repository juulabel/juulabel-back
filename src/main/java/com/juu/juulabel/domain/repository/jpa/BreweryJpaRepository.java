package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.alcohol.Brewery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BreweryJpaRepository extends JpaRepository<Brewery, Long> {
    Optional<Brewery> findByName(String name);

    Optional<Brewery> findFirstByName(String name);
}
