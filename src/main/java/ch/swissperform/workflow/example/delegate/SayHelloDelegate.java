package ch.swissperform.workflow.example.delegate;

import ch.swissperform.jtt.generated.openapi.api.DefaultApi;
import ch.swissperform.jtt.generated.openapi.invoker.ApiClient;
import ch.swissperform.jtt.generated.openapi.invoker.ApiException;
import ch.swissperform.jtt.generated.openapi.model.VersionInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SayHelloDelegate implements JavaDelegate {

    @Value("${applicaion.jtt.url}")
    private String jttUrl;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        log.info("executed sayHelloDelegate: {}", delegateExecution);
        log.info("url is: {}", jttUrl);

        DefaultApi defaultApi = new DefaultApi();

        ApiClient apiClient = defaultApi.getApiClient();
        apiClient.addDefaultHeader("Accept", "application/json");
        apiClient.setBasePath(jttUrl);

        log.info("OpenApi is using url: " + apiClient.getBasePath());

        defaultApi.setApiClient(apiClient);
        try {
            VersionInfo versionInfo = defaultApi.restInfoVersionGet();
            log.info("Got response from jtt. Version info: \n {}", versionInfo);

            Gson gson = new GsonBuilder().setPrettyPrinting()
                                         .create();
            String stringManifestJson = gson.toJson(defaultApi.restInfoManifestGet());
            log.info("Got response from jtt. Manifest info: \n {}", stringManifestJson);
        } catch (ApiException ex) {
            log.error("Failed to call jtt", ex);
            throw new RuntimeException(ex);
        }
    }

}
