package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.alcohol.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
