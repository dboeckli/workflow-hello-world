package ch.bpm.workflow.example.controller;

import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CamundaOpenApiRestControllerTest {

    @Test
    void returnsOpenApiResource() {
        assertThat(new CamundaOpenApiRestController().getFile()).contains("\"openapi\"");
    }

    @Test
    void returnsBadRequestWhenResourceCannotBeRead() {
        CamundaOpenApiRestController controller = new CamundaOpenApiRestController() {
            @Override
            InputStream openApiStream() {
                return new InputStream() {
                    @Override
                    public int read() throws IOException {
                        throw new IOException("read failed");
                    }
                };
            }
        };

        assertThatThrownBy(controller::getFile).isInstanceOfSatisfying(WebApplicationException.class,
                ex -> assertThat(ex.getResponse().getStatus()).isEqualTo(400));
    }

}
