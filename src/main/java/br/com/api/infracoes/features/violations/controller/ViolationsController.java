package br.com.api.infracoes.features.violations.controller;

import br.com.api.infracoes.docs.swagger.DocSwaggerViolationFindById;
import br.com.api.infracoes.docs.swagger.DocSwaggerViolationFindBySerial;
import br.com.api.infracoes.docs.swagger.DocSwaggerViolationSave;
import br.com.api.infracoes.features.violations.application.ViolationsService;
import br.com.api.infracoes.features.violations.dto.CreateViolationRequestDto;
import br.com.api.infracoes.features.violations.dto.ViolationFiltersRequestDto;
import br.com.api.infracoes.shared.annotations.PageParam;
import br.com.api.infracoes.shared.annotations.SizeParam;
import br.com.api.infracoes.shared.domain.entities.Violation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
public class ViolationsController {

    private final ViolationsService violationsService;

    @DocSwaggerViolationSave
    @PostMapping(path = "/violations")
    public ResponseEntity<Void> save(
            @Valid @ModelAttribute CreateViolationRequestDto violationRequestDto
                                    ) throws IOException {
        violationsService.save(violationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DocSwaggerViolationFindById
    @GetMapping(path = "/violations/{id}")
    public ResponseEntity<Violation> findById(@PathVariable Long id) {
        Violation violation = violationsService.findById(id);
        return ResponseEntity.ok(violation);
    }

    @DocSwaggerViolationFindBySerial
    @GetMapping(path = "/equipments/{serial}/violations")
    public ResponseEntity<Page<Violation>> findAllOrFilter(
            @PathVariable String serial,
            @PageParam int page,
            @SizeParam int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to
                                                          ) {
        ViolationFiltersRequestDto violationFiltersRequestDto = new ViolationFiltersRequestDto(serial, from, to);
        Page<Violation> violations = violationsService.findAll(violationFiltersRequestDto, page, size);
        return ResponseEntity.ok(violations);
    }

}
