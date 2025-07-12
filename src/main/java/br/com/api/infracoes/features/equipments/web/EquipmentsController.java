package br.com.api.infracoes.features.equipments.web;

import br.com.api.infracoes.features.equipments.annotations.SerialParam;
import br.com.api.infracoes.features.equipments.application.EquipmentsService;
import br.com.api.infracoes.features.equipments.domain.Equipment;
import br.com.api.infracoes.features.equipments.dto.CreateEquipmentRequestDto;
import br.com.api.infracoes.shared.annotations.PageParam;
import br.com.api.infracoes.shared.annotations.SizeParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Adiciona um novo equipamento",
            description = "Adicione um novo equipamento com as informaçõe fornecidas.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201", description = "Equipamento cadastrado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateEquipmentRequestDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos."
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Equipamento já cadastrado.",
                    content = @Content(mediaType = "text/plain")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Banco de dados indisponível."
            )
    })
    @PostMapping
    public ResponseEntity<Void> save(
            @RequestBody @Valid CreateEquipmentRequestDto equipmentRequestDto
                                    ) {
        equipmentsService.save(equipmentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Busca os equipamentos",
            description = "Lista todos os equipamentos. Inclusive os não ativos.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listagem paginada com os equipamentos."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Banco de dados indisponível."
            )
    })
    @GetMapping
    public ResponseEntity<Page<Equipment>> findAll(
            @PageParam int page,
            @SizeParam int size
                                                  ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Equipment> equipments = equipmentsService.findAll(pageable);
        return ResponseEntity.ok(equipments);
    }

    @Operation(
            summary = "Busca um equipamento específico",
            description = "Lista a partir do serial um equipamento específico.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Retorna o equipamento",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Equipment.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Equipamento não encontrado.",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Banco de dados indisponível.",
                    content = @Content()
            )
    })
    @GetMapping(path = "/{serial}")
    public ResponseEntity<Equipment> findBySerial(@PathVariable @SerialParam String serial) {
        Equipment equipment = equipmentsService.findBySerial(serial);
        return ResponseEntity.ok(equipment);
    }

}
