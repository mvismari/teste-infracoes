package br.com.api.infracoes.features.violations.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConditionalVelocityImpl.class)
@Documented
public @interface ConditionalVelocity {
    String message() default "Velocidade é obrigatório quando tipo é VELOCIDADE e maior que 0.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
