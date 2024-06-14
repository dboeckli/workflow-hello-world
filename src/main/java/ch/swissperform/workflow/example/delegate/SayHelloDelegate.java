package ch.swissperform.workflow.example.delegate;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Log4j2
public class SayHelloDelegate implements JavaDelegate {

    @Override

    public void execute(DelegateExecution delegateExecution) {
        log.info("executed sayHelloDelegate: {}", delegateExecution);
    }

}
