package ch.swissperform.workflow.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@Slf4j
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI();
    }
    // TODO: ADD camunda json/yaml to generated ui: See https://stackoverflow.com/questions/71156280/how-can-i-add-custom-json-object-to-openapi-spec-generated-by-springboot-springd

}
