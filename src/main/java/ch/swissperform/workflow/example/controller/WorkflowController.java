package ch.swissperform.workflow.example.controller;

import lombok.Builder;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/restapi/workflow", produces = "application/json")
public class WorkflowController {

    @Autowired
    public RuntimeService runtimeService;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HelloWorldWorklfowResponse> getInfo() {

        String processDefinitionKey = "hello-world-process";
        // when
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        return ResponseEntity.ok().body(createResponse(processInstance));

    }

    private HelloWorldWorklfowResponse createResponse(ProcessInstance processInstance) {
        return HelloWorldWorklfowResponse.builder()
                                         .caseInstanceId(processInstance.getCaseInstanceId())
                                         .processInstanceId(processInstance.getProcessDefinitionId())
                                         .id(processInstance.getId())
                                         .rootProcessInstanceId(processInstance.getRootProcessInstanceId())
                                         .processDefinitionId(processInstance.getProcessDefinitionId())
                                         .build();
    }

    @Builder
    public record HelloWorldWorklfowResponse(String caseInstanceId, String processDefinitionId, String rootProcessInstanceId, String id, String processInstanceId) {

    }

}
