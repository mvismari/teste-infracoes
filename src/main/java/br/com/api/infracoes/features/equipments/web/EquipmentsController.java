package br.com.api.infracoes.features.equipments.web;

import br.com.api.infracoes.features.equipments.application.EquipmentsService;
import br.com.api.infracoes.features.equipments.domain.Equipment;
import br.com.api.infracoes.features.equipments.dto.CreateEquipmentRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/equipments")
public class EquipmentsController {

    private final EquipmentsService equipmentsService;

    @PostMapping
    public ResponseEntity<Void> save(
            @RequestBody @Valid CreateEquipmentRequestDto equipmentRequestDto) {
        equipmentsService.save(equipmentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<Equipment>> findAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size
                                                  ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Equipment> equipments = equipmentsService.findAll(pageable);
        return ResponseEntity.ok(equipments);
    }

    @GetMapping(path = "/{serial}")
    public ResponseEntity<Equipment> findBySerial(@PathVariable String serial) {
        Equipment equipment = equipmentsService.findBySerial(serial);
        return ResponseEntity.ok(equipment);
    }

}
