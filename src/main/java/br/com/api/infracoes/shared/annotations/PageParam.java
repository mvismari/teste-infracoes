package br.com.api.infracoes.shared.annotations;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(description = "Página inicial da busca (inicia em 0)", example = "0")
@Min(0)
public @interface PageParam {
}
