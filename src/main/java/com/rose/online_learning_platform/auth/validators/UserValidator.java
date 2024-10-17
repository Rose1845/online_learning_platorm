package com.rose.online_learning_platform.auth.validators;

import com.rose.online_learning_platform.auth.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserValidator implements ConstraintValidator<ValidUser, String> {
    private final UserRepository userRepository;
    private boolean required;


    /**
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(ValidUser constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.required = constraintAnnotation.required();
    }

    /**
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!required && (value == null || value.isBlank())) {
            return true;
        }

        if (value == null || value.isBlank()) {
            return false;
        }
        return userRepository.findById(value).isPresent();
    }
}
