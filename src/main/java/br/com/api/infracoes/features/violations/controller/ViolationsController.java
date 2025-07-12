package br.com.api.infracoes.features.violations.controller;

import br.com.api.infracoes.features.violations.dto.CreateViolationRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/violations")
public class ViolationsController {

    @PostMapping
    public ResponseEntity<Void> save(
            @Valid @ModelAttribute CreateViolationRequestDto violationRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
