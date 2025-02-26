package ch.bpm.workflow.example;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.ProcessEngine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@ActiveProfiles(value = "test")
@Log4j2
class TestApplicationIT {

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        log.info("Testing Spring 6 Application {}", applicationContext.getApplicationName());
        log.info("### ProcessEngine started: {}", processEngine.getName());
        assertNotNull(applicationContext, "Application context should not be null");
        assertEquals( "default", processEngine.getName());
    }
}
