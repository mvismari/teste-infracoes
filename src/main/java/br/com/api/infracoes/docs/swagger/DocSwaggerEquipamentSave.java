package br.com.api.infracoes.docs.swagger;

import br.com.api.infracoes.features.equipments.dto.CreateEquipmentRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
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
        )})
public @interface DocSwaggerEquipamentSave {
}
