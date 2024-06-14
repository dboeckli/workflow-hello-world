package ch.swissperform.workflow.example.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PingResponse {

    String mavenGroupdId;

    String mavenArtifactId;

    String version;

    String vendor;

    String javaVersion;

    String mavenUser;

    String mavenTimeStamp;

    String gitCommitId;

    public PingResponse() {
        super();
    }
}
