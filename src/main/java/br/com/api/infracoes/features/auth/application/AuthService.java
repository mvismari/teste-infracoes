package br.com.api.infracoes.features.auth.application;

import br.com.api.infracoes.features.auth.dto.JwtResponseDto;
import br.com.api.infracoes.shared.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtResponseDto generateToken(String serviceName) {
        String token = jwtTokenProvider.generateToken(serviceName);
        return new JwtResponseDto(token, "Bearer", 7200);
    }

}
