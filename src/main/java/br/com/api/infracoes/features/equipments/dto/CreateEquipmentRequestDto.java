package br.com.api.infracoes.features.equipments.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateEquipmentRequestDto {

    @NotBlank(message = "{equipment.serial.required}")
    @Size(max = 100, message = "{equipment.serial.max}")
    private String serial;

    @NotBlank(message = "{equipment.model.required}")
    @Size(max = 50, message = "{equipment.model.max}")
    private String model;

    @NotBlank(message = "{equipment.address.required}")
    @Size(max = 200, message = "{equipment.address.max}")
    private String address;

    @DecimalMin(value = "-90.0", message = "{equipment.lat.min}")
    @DecimalMax(value = "90.0", message = "{equipment.lat.max}")
    private BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "{equipment.lon.min}")
    @DecimalMax(value = "180.0", message = "{equipment.lon.max}")
    private BigDecimal longitude;

    private Boolean active;
}
