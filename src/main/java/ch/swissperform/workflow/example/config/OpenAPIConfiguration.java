package ch.swissperform.workflow.example.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import io.swagger.v3.oas.models.OpenAPI;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
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
        /*
        try {
            OpenAPI openAPI = new OpenAPI();
            openAPI.setExtensions(camundaExtensions());
            return openAPI;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Failed to parse Camunda OpenAPI yaml", ex);
        }
        */
    }

    private Map<String, Object> camundaExtensions() throws JsonProcessingException {
        MapType type = mapper.getTypeFactory()
                             .constructMapType(Map.class, String.class, Object.class);
        Map<String, Object> stringObjectMap = new HashMap<>();

        stringObjectMap.put("api-definition", mapper.readValue(readFile("openapi.json"), type));
        log.debug("#### OpenAPI definition: {}", stringObjectMap);
        return stringObjectMap;
    }

    private String readFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            return Files.readString(Path.of(classLoader.getResource(fileName)
                                                       .getFile()));
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

}
