package ch.swissperform.workflow.example.config;

import ch.swissperform.jtt.generated.openapi.api.DefaultApi;
import ch.swissperform.jtt.generated.openapi.invoker.ApiClient;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
@Getter
public class RestApiConfiguration {

    @Value("${applicaion.jtt.protocol}")
    private String protocol;

    @Value("${applicaion.jtt.host}")
    private String host;

    @Value("${applicaion.jtt.port}")
    private String port;

    @Value("${applicaion.jtt.context}")
    private String context;

    @Bean
    public DefaultApi jttDefaultApi() {
        DefaultApi defaultApi = new DefaultApi();

        ApiClient apiClient = defaultApi.getApiClient();
        apiClient.addDefaultHeader("Accept", "application/json");
        apiClient.setBasePath(protocol + "://" + host + ":" + port + "/" + context);

        log.info("JTT rest url: " + apiClient.getBasePath());

        defaultApi.setApiClient(apiClient);
        return defaultApi;
    }

}
