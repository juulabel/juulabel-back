package com.juu.juulabel.api.validator;

import com.juu.juulabel.api.annotation.Rating;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class RatingValidator implements ConstraintValidator<Rating, Double> {

    private double min;
    private double max;

    @Override
    public void initialize(Rating annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return false;
        }

        return value >= min && value <= max && value % 0.25 == 0;
    }

}
