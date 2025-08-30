package com.adefaultdev.DSDataAnalyzer.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for REST API.
 * <p>
 * Configuring:
 * <ul>
 *   <li>API meta information (name, version, description)</li>
 *   <li>Developer's contacts</li>
 *   <li>License</li>
 *   <li>Test servers</li>
 * </ul>
 *
 * @see <a href="https://swagger.io/specification/">OpenAPI Specification</a>
 * @since 1.0.0
 * @author ADefaultDev
 */
@Configuration
public class OpenApiConfig {

    @Value("${openapi.dev-url}")
    private String devUrl;

    @Value("${openapi.prod-url}")
    private String prodUrl;

    /**
     * OpenAPI documentation configuration.
     *
     * @return configured instance of {@link OpenAPI}
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .servers(List.of(
                        new Server().url(devUrl).description("Development Server"),
                        new Server().url(prodUrl).description("Production Server")
                ))
                .info(new Info()
                        .title("DS Data Analyser")
                        .version("1.0.0")
                        .description("## Data analyser using DeepSeek for news processing")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }

}