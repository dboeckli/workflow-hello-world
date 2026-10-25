package ch.bpm.workflow.example.config.health;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class ApifirstJpaHealthIndicatorTest {

    private HttpServer server;

    private int port;

    private final AtomicReference<String> responseBody = new AtomicReference<>("{\"status\":\"UP\"}");

    private final AtomicReference<String> lastPath = new AtomicReference<>();

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/", exchange -> {
            lastPath.set(exchange.getRequestURI().getPath());
            byte[] body = responseBody.get().getBytes(StandardCharsets.UTF_8);
            if (body.length == 0) {
                exchange.sendResponseHeaders(200, -1);
            }
            else {
                exchange.sendResponseHeaders(200, body.length);
                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(body);
                }
            }
            exchange.close();
        });
        server.start();
        port = server.getAddress().getPort();
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
    }

    @Test
    void reportsUpForRootContext() {
        Health health = indicator("/").health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(lastPath.get()).isEqualTo("/actuator/health");
    }

    @Test
    void reportsUpForEmptyContext() {
        Health health = indicator("").health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(lastPath.get()).isEqualTo("/actuator/health");
    }

    @Test
    void usesContextInHealthUrl() {
        Health health = indicator("/api").health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(lastPath.get()).isEqualTo("/api/actuator/health");
    }

    @Test
    void reportsDownWhenStatusIsNotUp() {
        responseBody.set("{\"status\":\"DOWN\"}");

        assertThat(indicator("/").health().getStatus()).isEqualTo(Status.DOWN);
    }

    @Test
    void reportsDownForEmptyResponse() {
        responseBody.set("");

        assertThat(indicator("/").health().getStatus()).isEqualTo(Status.DOWN);
    }

    @Test
    void reportsDownWhenServerIsUnreachable() throws IOException {
        int closedPort;
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            closedPort = serverSocket.getLocalPort();
        }

        ApifirstJpaHealthIndicator indicator = new ApifirstJpaHealthIndicator("http", "localhost",
                String.valueOf(closedPort), "/");

        Health health = indicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsKey("error");
    }

    private ApifirstJpaHealthIndicator indicator(String context) {
        return new ApifirstJpaHealthIndicator("http", "localhost", String.valueOf(port), context);
    }

}
