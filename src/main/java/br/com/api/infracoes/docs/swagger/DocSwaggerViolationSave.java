package br.com.api.infracoes.docs.swagger;

import br.com.api.infracoes.features.violations.dto.CreateViolationRequestDto;
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
        summary = "Adiciona uma nova infração",
        description = "Adicione uma nova infração com as informações fornecidas.")
@ApiResponses({
        @ApiResponse(
                responseCode = "201", description = "Infração cadastrada com sucesso.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CreateViolationRequestDto.class))),
        @ApiResponse(
                responseCode = "422",
                description = "Equipamento inativo"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Equipamento não encontrado..",
                content = @Content(mediaType = "text/plain")
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Banco de dados indisponível."
        )})
public @interface DocSwaggerViolationSave {
}

