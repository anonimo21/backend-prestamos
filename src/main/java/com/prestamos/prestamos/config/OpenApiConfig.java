package com.prestamos.prestamos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración central de la documentación OpenAPI (Swagger) para la
 * API de préstamos.
 *
 * <p>Personaliza la información general (título, descripción, contacto,
 * licencia) que se muestra en la UI de Swagger y permite a los
 * consumidores de la API entender rápidamente su propósito y
 * versionado.</p>
 *
 * <p>Una vez iniciada la aplicación, la documentación se puede consultar
 * en:</p>
 * <ul>
 *   <li>UI Swagger: {@code http://localhost:8080/swagger-ui.html}</li>
 *   <li>Especificación OpenAPI en JSON:
 *       {@code http://localhost:8080/v3/api-docs}</li>
 *   <li>Especificación OpenAPI en YAML:
 *       {@code http://localhost:8080/v3/api-docs.yaml}</li>
 * </ul>
 */
@Configuration
public class OpenApiConfig {

    /**
     * Define el bean {@link OpenAPI} con los metadatos de la API.
     *
     * <p>Estos datos aparecen en la cabecera del documento OpenAPI y en
     * la página Swagger UI.</p>
     *
     * @return instancia de {@link OpenAPI} configurada con la
     *         información del proyecto.
     */
    @Bean
    public OpenAPI prestamosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Préstamos")
                        .description("API REST para la gestión integral de clientes y préstamos: alta, "
                                + "consulta, actualización y eliminación de clientes; creación de préstamos "
                                + "con cálculo automático de cuotas y montos; filtros por estado, validación "
                                + "de reglas de negocio y respuestas estandarizadas siguiendo RFC 7807 "
                                + "(ProblemDetail).")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Préstamos")
                                .email("dev@prestamos.local")
                                .url("https://prestamos.local"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor local de desarrollo"),
                        new Server()
                                .url("https://api.prestamos.local")
                                .description("Servidor de producción (ejemplo)")))
                .components(new Components());
    }
}
