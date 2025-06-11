package ch.bpm.workflow.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.RuntimeServiceImpl;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static ch.bpm.workflow.example.common.bpm.WorkflowConstants.INPUT_VARIABLE_NAME;
import static ch.bpm.workflow.example.common.bpm.WorkflowConstants.PROCESS_DEFINITION_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "test")
@TestPropertySource(properties = {
    "camunda.bpm.job-execution.enabled=false",
    "camunda.bpm.client.disable-auto-fetching=true",
    "spring.datasource.generate-unique-name=true",
    "spring.datasource.hikari.jdbc-url=jdbc:h2:mem:WorkflowRestControllerTest;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
})
@Slf4j
class WorkflowRestControllerTest {

    @InjectMocks
    WorkflowRestController workflowRestController;

    @Mock
    RuntimeServiceImpl runtimeServiceMock = new RuntimeServiceImpl();

    @Test
    void startProcess() {
        ExecutionEntity givenExecution = new ExecutionEntity();
        givenExecution.setId("id");
        givenExecution.setCaseInstanceId("caseInstanceId");
        givenExecution.setProcessInstanceId("processInstanceId");
        givenExecution.setRootProcessInstanceId("rootProcessInstanceId");
        givenExecution.setProcessDefinitionId("processDefinitionId");

        final String givenInput = "hallo";
        Mockito.when(runtimeServiceMock.startProcessInstanceByKey(eq(PROCESS_DEFINITION_KEY), eq(PROCESS_DEFINITION_KEY), eq(Map.of(INPUT_VARIABLE_NAME, givenInput)))).thenReturn(givenExecution);

        ResponseEntity<?> response = workflowRestController.startProcess(WorkflowRestController.InfoRequest.builder().input(givenInput).build());
        assertAll(
            () -> assertNotNull(response),
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertInstanceOf(WorkflowRestController.HelloWorldWorklfowResponse.class, response.getBody())
        );

        WorkflowRestController.HelloWorldWorklfowResponse responseBody = (WorkflowRestController.HelloWorldWorklfowResponse) response.getBody();
        assertAll(
            () -> assertEquals(givenExecution.getId(), responseBody.id()),
            () -> assertEquals(givenExecution.getCaseInstanceId(), responseBody.caseInstanceId()),
            () -> assertEquals(givenExecution.getProcessInstanceId(), responseBody.processInstanceId()),
            () -> assertEquals(givenExecution.getRootProcessInstanceId(), responseBody.rootProcessInstanceId()),
            () -> assertEquals(givenExecution.getProcessDefinitionId(), responseBody.processDefinitionId())
        );
    }
}
