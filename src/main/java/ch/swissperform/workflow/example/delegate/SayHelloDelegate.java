package ch.swissperform.workflow.example.delegate;

import ch.swissperform.jtt.generated.openapi.api.DefaultApi;
import ch.swissperform.jtt.generated.openapi.invoker.ApiException;
import ch.swissperform.jtt.generated.openapi.model.VersionInfo;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SayHelloDelegate implements JavaDelegate {

    final DefaultApi jttDefaultApi;

    @Autowired
    public SayHelloDelegate(DefaultApi jttDefaultApi) {
        this.jttDefaultApi = jttDefaultApi;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {

        log.info("executing sayHelloDelegate: {}", delegateExecution);

        try {
            VersionInfo versionInfo = jttDefaultApi.restInfoVersionGet();
            log.info("Got response from jtt. Version info: \n {}", versionInfo);

            /* TODO: commented out for unit test
            Gson gson = new GsonBuilder().setPrettyPrinting()
                                         .create();
            String stringManifestJson = gson.toJson(jttDefaultApi.restInfoManifestGet());
            log.info("Got response from jtt. Manifest info: \n {}", stringManifestJson);
            */
        } catch (ApiException ex) {
            log.error("Failed to call jtt", ex);
            throw new RuntimeException(ex);
        }
    }

}
