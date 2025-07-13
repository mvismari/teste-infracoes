package br.com.api.infracoes.shared.annotations;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(description = "Página inicial da busca (inicia em 0)", example = "0")
@Min(0)
public @interface PageParam {
}
