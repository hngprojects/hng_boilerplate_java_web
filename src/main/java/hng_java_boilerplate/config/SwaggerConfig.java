package hng_java_boilerplate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SwaggerConfig{

    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/v1/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("BoilerPlate").version("1.0")
                        .description("A standardized and reusable set of code " +
                                "used as a starting point for developing applications"))
                .servers(Arrays.asList(
                        new Server().url("https://api-java.boilerplate.hng.tech").description("My App Server URL"),
                        new Server().url("http://localhost:8080").description("Local server"),
                        new Server().url("https://deployment.api-java.boilerplate.hng.tech").description("My App " +
                                "deployment Server URL"),
                        new Server().url("https://staging.api-java.boilerplate.hng.tech").description("My App Server URL")



                        ))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        ))
                .security(Collections.singletonList(
                        new SecurityRequirement().addList("bearerAuth")
                ));

    }
}
