package br.com.api.infracoes.features.violations.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ValidateTypeImpl implements ConstraintValidator<ValidateType, Enum<?>> {
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(ValidateType annotation) {
        this.enumClass = annotation.enumClass();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return Arrays.asList(enumClass.getEnumConstants()).contains(value);
    }
}
