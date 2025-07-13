package br.com.api.infracoes.docs.swagger;

import br.com.api.infracoes.shared.domain.entities.Violation;
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
        summary = "Busca uma violação específica",
        description = "Lista detalhes a partir do ID da violação.")
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Retorna a violação",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Violation.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Violação não encontrada.",
                content = @Content()
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Banco de dados indisponível.",
                content = @Content()
        )
})
public @interface DocSwaggerViolationFindById {
}
