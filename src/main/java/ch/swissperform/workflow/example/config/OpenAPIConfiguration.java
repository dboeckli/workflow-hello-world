package ch.swissperform.workflow.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ch.swissperform.workflow.example.common.LogMessage.READING_CONFIG_CLASS;

@Configuration
@Data
@Slf4j
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI();
    }

    @PostConstruct
    public void init() {
        log.info(READING_CONFIG_CLASS.getMessage(), this.getClass().getName(), this);
    }

}
