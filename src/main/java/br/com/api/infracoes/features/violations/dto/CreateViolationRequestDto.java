package br.com.api.infracoes.features.violations.dto;

import br.com.api.infracoes.features.violations.annotations.ConditionalVelocity;
import br.com.api.infracoes.features.violations.annotations.ValidFile;
import br.com.api.infracoes.features.violations.annotations.ValidateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@ConditionalVelocity
public class CreateViolationRequestDto {

    public enum TypeViolations {
        Velocity, StopCrossWalking
    }

    @NotBlank(message = "{equipment.serial.required}")
    @Size(max = 100, message = "{equipment.serial.max}")
    private String serial;

    @NotNull(message = "{violation.occurence-date.required}")
    @Pattern(
            regexp = "^\\d{4}[\\-‐]\\d{2}[\\-‐]\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{3})?Z$",
            message = "{violation.occurence-date.invalid-format}"
    )
    private String occurrenceDateUtc;

    private Double measuredSpeed;
    private Double consideredSpeed;
    private Double regulatedSpeed;

    @ValidFile(message = "{violation.file.invalid-format}")
    private MultipartFile picture;

    @NotNull(message = "{violation.type.required}")
    @ValidateType(enumClass = TypeViolations.class, message = "{violation.type.required}")
    private TypeViolations type;

}
