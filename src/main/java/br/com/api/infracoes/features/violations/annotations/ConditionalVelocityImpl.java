package br.com.api.infracoes.features.violations.annotations;

import br.com.api.infracoes.features.violations.dto.CreateViolationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class ConditionalVelocityImpl implements ConstraintValidator<ConditionalVelocity, CreateViolationRequestDto> {

    @Override
    public boolean isValid(CreateViolationRequestDto dto, ConstraintValidatorContext context) {
        if (dto == null || !CreateViolationRequestDto.TypeViolations.Velocity.equals(dto.getType())) {
            return true;
        }

        List<String> invalidFields = new ArrayList<>();
        if (dto.getMeasuredSpeed() == null || dto.getMeasuredSpeed() <= 0) {
            invalidFields.add("measuredSpeed");
        }

        if (dto.getConsideredSpeed() == null || dto.getConsideredSpeed() <= 0) {
            invalidFields.add("consideredSpeed");
        }

        if (dto.getRegulatedSpeed() == null || dto.getRegulatedSpeed() <= 0) {
            invalidFields.add("regulatedSpeed");
        }

        if (!invalidFields.isEmpty()) {
            context.disableDefaultConstraintViolation();
            invalidFields.forEach(field -> {
                context.buildConstraintViolationWithTemplate(getErrorMessage(field))
                        .addPropertyNode(field)
                        .addConstraintViolation();
            });

            return false;
        }

        return true;
    }

    private String getErrorMessage(String field) {
        return switch (field) {
            case "measuredSpeed" -> "Velocidade medida é obrigatória e deve ser maior que 0";
            case "consideredSpeed" -> "Velocidade considerada é obrigatória e deve ser maior que 0";
            case "regulatedSpeed" -> "Velocidade regulamentada é obrigatória e deve ser maior que 0";
            default -> "Velocidade é obrigatória e deve ser maior que 0";
        };
    }

}

