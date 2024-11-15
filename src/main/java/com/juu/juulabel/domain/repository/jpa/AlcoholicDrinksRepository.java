package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.alcohol.AlcoholicDrinks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlcoholicDrinksRepository extends JpaRepository<AlcoholicDrinks, Long> {
    Optional<AlcoholicDrinks> findByName(String name);
}
