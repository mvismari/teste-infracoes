package br.com.api.infracoes.features.violations.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidFileImpl.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFile {
    String message() default "Arquivo inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    long maxSize() default 1024 * 1024;
    String[] allowedTypes() default {".jpg", ".jpeg", ".png", ".pdf"};
}
