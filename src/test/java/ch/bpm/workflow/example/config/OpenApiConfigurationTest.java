package ch.bpm.workflow.example.config;

import com.sun.net.httpserver.HttpServer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.info.BuildProperties;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OpenApiConfigurationTest {

    private static final String OPENAPI_JSON = """
            {
              "openapi": "3.0.0",
              "info": {"title": "Camunda", "version": "1.0.0"},
              "servers": [{"url": "http://old-server"}],
              "paths": {
                "/engine-rest/engine": {
                  "get": {
                    "operationId": "getEngines",
                    "responses": {"200": {"description": "OK"}}
                  }
                }
              },
              "components": {
                "schemas": {
                  "EngineDto": {"type": "object"}
                }
              }
            }
            """;

    private static final String OPENAPI_JSON_WITHOUT_COMPONENTS = """
            {
              "openapi": "3.0.0",
              "info": {"title": "Camunda", "version": "1.0.0"},
              "servers": [{"url": "http://old-server"}],
              "paths": {
                "/engine-rest/engine": {
                  "get": {
                    "operationId": "getEngines",
                    "responses": {"200": {"description": "OK"}}
                  }
                }
              }
            }
            """;

    private HttpServer server;

    private int port;

    private final AtomicReference<String> responseBody = new AtomicReference<>(OPENAPI_JSON);

    private final AtomicInteger responseStatus = new AtomicInteger(200);

    private MockEnvironment environment;

    private OpenApiConfiguration configuration;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/restapi/camunda", exchange -> {
            byte[] body = responseBody.get().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(responseStatus.get(), body.length);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(body);
            }
        });
        server.start();
        port = server.getAddress().getPort();

        Properties properties = new Properties();
        properties.setProperty("name", "workflow-hello-world");
        properties.setProperty("version", "1.2.3");
        environment = new MockEnvironment();
        environment.setProperty("local.server.port", String.valueOf(port));
        configuration = new OpenApiConfiguration(new BuildProperties(properties), environment);
        ReflectionTestUtils.setField(configuration, "servletContextPath", "");
        ReflectionTestUtils.setField(configuration, "username", "user");
        ReflectionTestUtils.setField(configuration, "password", "secret");
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void customizerSetsBuildInfoAndActuatorTagDescription() {
        OpenAPI openApi = openApiWithInfo();
        openApi.addTagsItem(new Tag().name("Actuator")).addTagsItem(new Tag().name("Other"));

        configuration.customGlobalHeaderOpenApiCustomizer().customise(openApi);

        assertThat(openApi.getInfo().getTitle()).isEqualTo("workflow-hello-world");
        assertThat(openApi.getInfo().getVersion()).isEqualTo("1.2.3");
        assertThat(openApi.getTags()).extracting(Tag::getName).containsExactly("Actuator", "Other");
        assertThat(openApi.getTags().get(0).getDescription())
            .isEqualTo("Spring Boot Actuator endpoints for monitoring and managing the application");
        assertThat(openApi.getTags().get(1).getDescription()).isNull();
    }

    @Test
    void customizerToleratesMissingTags() {
        OpenAPI openApi = openApiWithInfo();

        configuration.customGlobalHeaderOpenApiCustomizer().customise(openApi);

        assertThat(openApi.getTags()).isNull();
        assertThat(openApi.getInfo().getTitle()).isEqualTo("workflow-hello-world");
    }

    @Test
    void restApiAddsBasicAuthSecurityScheme() {
        GroupedOpenApi groupedOpenApi = configuration.restapiApi(configuration.customGlobalHeaderOpenApiCustomizer());
        OpenAPI openApi = openApiWithInfo();

        groupedOpenApi.getOpenApiCustomizers().forEach(customizer -> customizer.customise(openApi));

        SecurityScheme securityScheme = openApi.getComponents().getSecuritySchemes().get("basicAuth");
        assertThat(securityScheme.getType()).isEqualTo(SecurityScheme.Type.HTTP);
        assertThat(securityScheme.getScheme()).isEqualTo("basic");
        assertThat(openApi.getSecurity()).singleElement()
            .satisfies(requirement -> assertThat(requirement).containsKey("basicAuth"));
    }

    @Test
    void camundaCustomizerMergesExternalOpenApi() {
        GroupedOpenApi groupedOpenApi = configuration
            .camnundaEngineRestApi(configuration.customGlobalHeaderOpenApiCustomizer());
        OpenAPI openApi = openApiWithInfo();
        openApi.setPaths(new Paths());

        groupedOpenApi.getOpenApiCustomizers().forEach(customizer -> customizer.customise(openApi));

        assertThat(openApi.getPaths()).containsKey("/engine-rest/engine");
        assertThat(openApi.getComponents().getSchemas()).containsKey("EngineDto");
        assertThat(openApi.getServers()).singleElement()
            .satisfies(server -> assertThat(server.getUrl()).isEqualTo("http://localhost:" + port + "/engine-rest"));
    }

    @Test
    void camundaCustomizerSkipsMissingExternalComponents() {
        responseBody.set(OPENAPI_JSON_WITHOUT_COMPONENTS);
        GroupedOpenApi groupedOpenApi = configuration
            .camnundaEngineRestApi(configuration.customGlobalHeaderOpenApiCustomizer());
        OpenAPI openApi = openApiWithInfo();
        openApi.setPaths(new Paths());

        groupedOpenApi.getOpenApiCustomizers().forEach(customizer -> customizer.customise(openApi));

        assertThat(openApi.getPaths()).containsKey("/engine-rest/engine");
        assertThat(openApi.getComponents()).isNull();
        assertThat(openApi.getServers()).singleElement()
            .satisfies(server -> assertThat(server.getUrl()).isEqualTo("http://localhost:" + port + "/engine-rest"));
    }

    @Test
    void camundaCustomizerWrapsFailures() {
        responseStatus.set(500);
        GroupedOpenApi groupedOpenApi = configuration
            .camnundaEngineRestApi(configuration.customGlobalHeaderOpenApiCustomizer());
        OpenAPI openApi = openApiWithInfo();
        openApi.setPaths(new Paths());

        assertThatThrownBy(
                () -> groupedOpenApi.getOpenApiCustomizers().forEach(customizer -> customizer.customise(openApi)))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Failed to load camunda OpenAPI definition.");
    }

    private static OpenAPI openApiWithInfo() {
        return new OpenAPI().info(new Info());
    }

}
