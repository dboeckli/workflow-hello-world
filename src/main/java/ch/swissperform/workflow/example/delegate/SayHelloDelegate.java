package ch.swissperform.workflow.example.delegate;

import ch.guru.springframework.apifirst.client.CustomerApi;
import ch.guru.springframework.apifirst.model.CustomerDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
public class SayHelloDelegate implements JavaDelegate {

    final CustomerApi customerApi;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        log.info("executing sayHelloDelegate: {}", delegateExecution);

        List<CustomerDto> customers = customerApi.listCustomers();
        log.info("Got response from apifirst. Customers: \n {}", customers);
    }

}
