package br.com.api.infracoes.features.equipments.annotations.docs;

import br.com.api.infracoes.features.equipments.domain.Equipment;
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
public @interface DocSwaggerFindBySerial {
}
