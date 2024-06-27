package ch.swissperform.workflow.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@Slf4j
public class OpenAPIConfiguration {

    private static ObjectMapper mapper = new ObjectMapper();

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI();
    }

}
