package ch.bpm.workflow.example.config.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ApifirstJpaHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate;
    private final String authServerUrl;

    public ApifirstJpaHealthIndicator(@Value("${application.apifirst-server-jpa.protocol}") String protocol,
                                      @Value("${application.apifirst-server-jpa.host}") String host,
                                      @Value("${application.apifirst-server-jpa.port}") String port,
                                      @Value("${application.apifirst-server-jpa.context}") String context) {

        this.restTemplate = new RestTemplateBuilder().build();

        String url = protocol + "://" + host + ":" + port;
        if (context.equals("/") || context.isEmpty()) {
            this.authServerUrl = url;
        } else {
            this.authServerUrl = url + "/" + context;
        }
    }

    @Override
    public Health health() {
        try {
            String response = restTemplate.getForObject(authServerUrl + "/actuator/health", String.class);
            if (response != null && response.contains("\"status\":\"UP\"")) {
                return Health.up().build();
            } else {
                log.warn("ApiFirst-Jpa server is not reporting UP status at {}", authServerUrl);
                return Health.down().build();
            }
        } catch (Exception e) {
            log.warn("ApiFirst-Jpa is not reachable at {}", authServerUrl, e);
            return Health.down(e).build();
        }
    }

}
