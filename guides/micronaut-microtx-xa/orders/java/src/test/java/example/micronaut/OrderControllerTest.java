/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.Transferable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest(transactional = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderControllerTest implements TestPropertyProvider {

    static final int PORT = freePort();

    static final GenericContainer<?> COORDINATOR = new GenericContainer<>("container-registry.oracle.com/database/otmm:24.4.1")
        .withCopyToContainer(Transferable.of("""
            tmmConfiguration:
              tmmAppName: tcs
              listenAddr: 0.0.0.0:9000
              internalAddr: http://localhost:9000
              externalUrl: http://localhost:9000
              xaCoordinator:
                enabled: true
              storage:
                type: memory
              authentication:
                enabled: false
              authorization:
                enabled: false
              serveTLS:
                enabled: false
            """), "/app/config/tcs.yaml")
        .withEnv("CONFIG_FILE", "/app/config/tcs.yaml")
        .withEnv("SERVE_TLS_ENABLED", "false")
        .withExposedPorts(9000)
        .waitingFor(Wait.forHttp("/health"));

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    PurchaseOrderRepository orders;

    @Override
    public Map<String, String> getProperties() {
        Testcontainers.exposeHostPorts(PORT);
        COORDINATOR.start();
        return Map.of(
            "micronaut.server.port", String.valueOf(PORT),
            "microtx.coordinator-url", "http://" + COORDINATOR.getHost() + ":" + COORDINATOR.getMappedPort(9000) + "/api/v1",
            "microtx.xa.participant-url", "http://host.testcontainers.internal:" + PORT,
            "micronaut.http.services.stock.url", "http://localhost:" + PORT,
            "stock.stub", "true" // <1>
        );
    }

    @BeforeEach
    void clean() {
        orders.deleteAll();
    }

    @Test
    void anOrderCommitsWhenTheStockIsReserved() {
        HttpResponse<PurchaseOrder> response = httpClient.toBlocking().exchange(
            HttpRequest.POST("/orders", new OrderRequest("book", 2)), PurchaseOrder.class);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals(1, orders.count()); // <2>
    }

    @Test
    void anOrderRollsBackWhenTheStockCannotBeReserved() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
            httpClient.toBlocking().exchange(HttpRequest.POST("/orders", new OrderRequest("book", 20)), PurchaseOrder.class));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatus());
        assertEquals(0, orders.count()); // <3>
    }

    private static int freePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
