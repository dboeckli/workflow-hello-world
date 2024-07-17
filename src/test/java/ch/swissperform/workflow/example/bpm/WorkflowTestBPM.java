package ch.swissperform.workflow.example.bpm;

import ch.swissperform.jtt.generated.openapi.api.DefaultApi;
import ch.swissperform.jtt.generated.openapi.model.VersionInfo;
import ch.swissperform.workflow.example.config.JTTRestApiConfiguration;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest(args = {"--spring.datasource.url=jdbc:h2:mem:WorkflowTestBPM;DB_CLOSE_ON_EXIT=FALSE", "--camunda.bpm.job-execution.enabled=false"})
@Testcontainers
@Deployment(resources = "process.bpmn")
@Log4j2
class WorkflowTestBPM {

    @Autowired
    public RuntimeService runtimeService;

    @Container
    public MockServerContainer mockServer = new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.14.0"));

    @Autowired
    DefaultApi defaultApi;

    @Autowired
    JTTRestApiConfiguration JTTRestApiConfiguration;

    private MockServerClient mockServerClient;

    @BeforeEach
    public void setup() {
        mockServerClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
        defaultApi.getApiClient()
                  .setBasePath(JTTRestApiConfiguration.getProtocol() + "://" + mockServer.getHost() + ":" + mockServer.getServerPort() + "/" + JTTRestApiConfiguration.getContext());
    }

    @Test
    void shouldExecuteHappyPath() {
        createExpectedMockserverResponse();

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

    private void createExpectedMockserverResponse() {
        VersionInfo givenVersionInfo = new VersionInfo();
        givenVersionInfo.setApplicationVersion("applicationVersion");
        givenVersionInfo.setWildflyProductVersion("wildflyProductVersion");
        givenVersionInfo.setWildflyReleaseVersion("wildflyReleaseVersion");
        mockServerClient.when(request().withMethod("GET").withPath("/swp-jtt/rest/info/version")).respond(response().withStatusCode(200).withBody(givenVersionInfo.toJson()));
    }

}
