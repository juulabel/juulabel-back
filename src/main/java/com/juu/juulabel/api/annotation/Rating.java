package com.juu.juulabel.api.annotation;

import com.juu.juulabel.api.validator.RatingValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Constraint : RatingValidator 클래스를 통한 검증 (Java Bean Validation API)
 */
@Constraint(validatedBy = RatingValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Rating {
    String message() default "달점 입력이 잘못되었습니다. (0.00 - 5.00, 0.25 단위)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    double min() default 0.00;
    double max() default 5.00;
}
