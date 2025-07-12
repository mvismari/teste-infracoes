package br.com.api.infracoes.features.equipments.controller;

import br.com.api.infracoes.features.equipments.annotations.SerialParam;
import br.com.api.infracoes.docs.swagger.DocSwaggerEquipamentFindAll;
import br.com.api.infracoes.docs.swagger.DocSwaggerEquipamentFindBySerial;
import br.com.api.infracoes.docs.swagger.DocSwaggerEquipamentSave;
import br.com.api.infracoes.features.equipments.application.EquipmentsService;
import br.com.api.infracoes.features.equipments.domain.Equipment;
import br.com.api.infracoes.features.equipments.dto.CreateEquipmentRequestDto;
import br.com.api.infracoes.shared.annotations.PageParam;
import br.com.api.infracoes.shared.annotations.SizeParam;
import jakarta.validation.Valid;
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

    @DocSwaggerEquipamentSave
    @PostMapping
    public ResponseEntity<Void> save(
            @RequestBody @Valid CreateEquipmentRequestDto equipmentRequestDto
                                    ) {
        equipmentsService.save(equipmentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DocSwaggerEquipamentFindAll
    @GetMapping
    public ResponseEntity<Page<Equipment>> findAll(
            @PageParam int page,
            @SizeParam int size
                                                  ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Equipment> equipments = equipmentsService.findAll(pageable);
        return ResponseEntity.ok(equipments);
    }

    @DocSwaggerEquipamentFindBySerial
    @GetMapping(path = "/{serial}")
    public ResponseEntity<Equipment> findBySerial(@PathVariable @SerialParam String serial) {
        Equipment equipment = equipmentsService.findBySerial(serial);
        return ResponseEntity.ok(equipment);
    }

}
