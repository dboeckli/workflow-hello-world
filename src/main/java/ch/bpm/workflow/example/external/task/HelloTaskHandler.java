package ch.bpm.workflow.example.external.task;

import ch.bpm.workflow.example.common.bpm.variable.token.TokenVariable;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static ch.bpm.workflow.example.common.bpm.WorkflowConstants.PROCESS_DEFINITION_KEY;
import static ch.bpm.workflow.example.common.bpm.variable.token.TokenStatus.RUNNING;
import static ch.bpm.workflow.example.common.bpm.variable.token.TokenVariable.TOKEN_VARIABLE_NAME;

@Component
@ExternalTaskSubscription(
    topicName = "sayHelloTopic",
    processDefinitionKey = PROCESS_DEFINITION_KEY,
    includeExtensionProperties = true
)
@Slf4j
public class HelloTaskHandler implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        log.info("### Received external task: {} with variables {}", externalTask.getId(), externalTask.getAllVariables());

        TokenVariable tokenVariable = externalTask.getVariable(TOKEN_VARIABLE_NAME);
        tokenVariable.setStatus(RUNNING);

        externalTaskService.complete(externalTask, Collections.singletonMap(TOKEN_VARIABLE_NAME, tokenVariable));
     }
}
