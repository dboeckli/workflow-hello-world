package ch.bpm.workflow.example.bpm;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.complete;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.hikari.jdbc-url=jdbc:h2:mem:WorkflowTestBPM;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.hikari.pool-name=WorkflowTestBPM",
        "camunda.bpm.job-execution.enabled=false"
})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Deployment(resources = "process.bpmn")
@Log4j2
@ActiveProfiles(value = "local")
class WorkflowTestBPM {

    @Autowired
    public RuntimeService runtimeService;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        log.info("DataSource before test: {}", dataSource);
    }

    @AfterEach
    void tearDown() {
        log.info("DataSource after test: {}", dataSource);
    }

    @Test
    void shouldExecuteHappyPath() {
        // given
        String processDefinitionKey = "hello-world-process";

        // when
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

        // then
        assertThat(processInstance).isStarted().task().hasDefinitionKey("say-hello").hasCandidateUser("admin").isNotAssigned();
        assertThat(processInstance).isWaitingAt("say-hello");

        complete(task());

        assertThat(processInstance).hasPassed("Activity_say_hello-via_delegate");
        assertThat(processInstance).isEnded();
    }

}
