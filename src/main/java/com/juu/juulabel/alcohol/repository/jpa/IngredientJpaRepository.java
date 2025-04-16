package com.juu.juulabel.alcohol.repository.jpa;

import com.juu.juulabel.alcohol.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientJpaRepository extends JpaRepository<Ingredient, Long> {
}
