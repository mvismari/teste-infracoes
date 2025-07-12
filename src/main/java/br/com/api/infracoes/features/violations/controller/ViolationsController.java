package br.com.api.infracoes.features.violations.controller;

import br.com.api.infracoes.features.violations.application.ViolationsService;
import br.com.api.infracoes.features.violations.dto.CreateViolationRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/violations")
public class ViolationsController {

    private final ViolationsService violationsService;

    @PostMapping
    public ResponseEntity<Void> save(
            @Valid @ModelAttribute CreateViolationRequestDto violationRequestDto
                                    ) throws IOException {
        violationsService.save(violationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
