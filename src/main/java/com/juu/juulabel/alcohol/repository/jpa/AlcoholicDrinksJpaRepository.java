package com.juu.juulabel.alcohol.repository.jpa;

import com.juu.juulabel.alcohol.domain.AlcoholicDrinks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlcoholicDrinksJpaRepository extends JpaRepository<AlcoholicDrinks, Long> {
    Optional<AlcoholicDrinks> findByName(String name);
}
