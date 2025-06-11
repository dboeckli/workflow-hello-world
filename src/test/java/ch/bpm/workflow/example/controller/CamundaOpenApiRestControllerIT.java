package ch.bpm.workflow.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "local")
@TestPropertySource(properties = {
    "camunda.bpm.job-execution.enabled=false",
    "camunda.bpm.client.disable-auto-fetching=true",
    "spring.datasource.generate-unique-name=true",
    "spring.datasource.hikari.jdbc-url=jdbc:h2:mem:CamundaOpenApiRestControllerIT;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
})
//@DirtiesContext(classMode = BEFORE_CLASS)
@Deployment(resources = "process.bpmn")
@Slf4j
class CamundaOpenApiRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getOpenApiFile() throws Exception {
        this.mockMvc.perform(get("/restapi/camunda")
            .with(httpBasic("camunda-admin", "camunda-admin-password")))
            .andExpect(status().isOk()).andExpect(content().json(createResponse()));
    }

    @Test
    void getOpenApiUnauthorized() throws Exception {
        this.mockMvc.perform(get("/restapi/camunda"))
            .andExpect(status().isUnauthorized());
    }

    private String createResponse() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/openapi.json");
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

}
