package br.com.api.infracoes.docs.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
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
public @interface DocSwaggerEquipamentFindAll {
}
