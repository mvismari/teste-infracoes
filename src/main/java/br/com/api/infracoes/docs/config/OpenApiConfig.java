package br.com.api.infracoes.docs.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                              .title("Infrações - API")
                              .description("API para gerenciamento de equipamentos e infrações.")
                              .version("1.0.0")
                              .contact(new Contact()
                                               .name("Responsável:")
                                               .email("marlon.vismari@gmail.com"))
                     );
    }
}
