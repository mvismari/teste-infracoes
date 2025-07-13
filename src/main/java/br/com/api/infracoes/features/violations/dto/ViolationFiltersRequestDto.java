package br.com.api.infracoes.features.violations.dto;

import java.time.OffsetDateTime;


public record ViolationFiltersRequestDto(String serial, OffsetDateTime from, OffsetDateTime to) {}
