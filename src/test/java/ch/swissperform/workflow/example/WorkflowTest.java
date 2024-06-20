package ch.swissperform.workflow.example;

import ch.swissperform.jtt.generated.openapi.api.DefaultApi;
import ch.swissperform.jtt.generated.openapi.model.VersionInfo;
import ch.swissperform.workflow.example.config.RestApiConfiguration;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.complete;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest
@Testcontainers
@RunWith(SpringRunner.class)
@Log4j2
// TODO: migrate to junit5
public class WorkflowTest extends AbstractProcessEngineRuleTest {

    @Autowired
    public RuntimeService runtimeService;

    @Container
    public MockServerContainer mockServer = new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.14.0"));

    @Autowired
    DefaultApi defaultApi;

    @Autowired
    RestApiConfiguration restApiConfiguration;

    private MockServerClient mockServerClient;

    @Before
    public void setup() {
        mockServer.start();
        mockServerClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
        defaultApi.getApiClient().setBasePath(restApiConfiguration.getProtocol() + "://" + mockServer.getHost() + ":" + mockServer.getServerPort() + "/" + restApiConfiguration.getContext());

        log.info("Set DefaultApi: " + defaultApi.getApiClient().getBasePath());
    }

    @After
    public void tearDown() {
        mockServer.stop();
        mockServerClient.stop();
    }

    @Test
    public void shouldExecuteHappyPath() {
        VersionInfo givenVersionInfo = new VersionInfo();
        givenVersionInfo.setApplicationVersion("applicationVersion");
        givenVersionInfo.setWildflyProductVersion("wildflyProductVersion");
        givenVersionInfo.setWildflyReleaseVersion("wildflyReleaseVersion");
        mockServerClient.when(
                request()
                        .withMethod("GET")
                        .withPath("/swp-jtt/rest/info/version")
        ).respond(
                response()
                        .withStatusCode(200)
                        .withBody(givenVersionInfo.toJson())
        );

        // given
        String processDefinitionKey = "hello-world-process";

        // when
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

        // then
        assertThat(processInstance).isStarted()
                                   .task()
                                   .hasDefinitionKey("say-hello")
                                   .hasCandidateUser("admin")
                                   .isNotAssigned();

        assertThat(processInstance).isWaitingAt("say-hello");

        complete(task());

        assertThat(processInstance).hasPassed("Activity_say_hello-via_delegate");

        assertThat(processInstance).isEnded();
    }

}
