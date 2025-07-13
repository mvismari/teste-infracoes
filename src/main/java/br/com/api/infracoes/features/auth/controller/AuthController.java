package br.com.api.infracoes.features.auth.controller;

import br.com.api.infracoes.features.auth.application.AuthService;
import br.com.api.infracoes.features.auth.dto.JwtResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth")
    public ResponseEntity<JwtResponseDto> auth() {
        JwtResponseDto jwtDto = authService.generateToken("InfracoesService");
        return ResponseEntity.ok(jwtDto);
    }

}
