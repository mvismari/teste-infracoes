package br.com.api.infracoes.features.equipments.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Equipment {
    private String serial;
    private String model;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean active;
}
