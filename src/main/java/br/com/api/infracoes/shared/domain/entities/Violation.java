package br.com.api.infracoes.shared.domain.entities;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Violation {
    private String serial;
    private OffsetDateTime occurrenceDateUtc;
    private Double measuredSpeed;
    private Double consideredSpeed;
    private Double regulatedSpeed;
    private String picture;
    private String type;
}
