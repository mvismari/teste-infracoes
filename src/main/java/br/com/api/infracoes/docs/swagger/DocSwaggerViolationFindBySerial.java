package br.com.api.infracoes.docs.swagger;

import br.com.api.infracoes.shared.domain.entities.Equipment;
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
        summary = "Busca as violações a partir do serial do equipamento",
        description = "Lista todas as violações de um equipamento.")
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Listagem paginada com as violações."
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Banco de dados indisponível."
        )
})
public @interface DocSwaggerViolationFindBySerial {
}
