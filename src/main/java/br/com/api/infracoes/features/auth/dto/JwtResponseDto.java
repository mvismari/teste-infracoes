package br.com.api.infracoes.features.auth.dto;

public record JwtResponseDto(String accessToken, String tokenType, int expiresIn ) {
}
