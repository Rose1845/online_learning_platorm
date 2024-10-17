package com.rose.online_learning_platform.auth.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Documented
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserValidator.class)
public @interface ValidUser {
    String message() default "User is invalid";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    boolean required() default true;
}
