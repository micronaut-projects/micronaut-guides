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
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest(transactional = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StockControllerTest implements TestPropertyProvider {

    static final int PORT = freePort();

    static final GenericContainer<?> COORDINATOR = new GenericContainer<>("container-registry.oracle.com/database/otmm:24.4.1") // <1>
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
    StockRepository stock;

    @Override
    public Map<String, String> getProperties() {
        Testcontainers.exposeHostPorts(PORT); // <2>
        COORDINATOR.start();
        return Map.of(
            "micronaut.server.port", String.valueOf(PORT),
            "microtx.coordinator-url", coordinatorUrl(),
            "microtx.xa.participant-url", "http://host.testcontainers.internal:" + PORT // <3>
        );
    }

    @BeforeEach
    void stock() {
        stock.deleteAll();
        stock.save(new Stock("book", 10));
    }

    @Test
    void aCommittedReservationReducesTheStock() throws Exception {
        String gtrid = begin(); // <4>

        httpClient.toBlocking().exchange(HttpRequest.POST("/stock/book/reserve?quantity=3", "")
            .header("Link", link(gtrid))); // <5>
        assertEquals(10, stock.findById("book").orElseThrow().quantity()); // <6>

        assertEquals(200, coordinator("PUT", gtrid).statusCode()); // <7>
        assertEquals(7, stock.findById("book").orElseThrow().quantity());
    }

    @Test
    void aRolledBackReservationKeepsTheStock() throws Exception {
        String gtrid = begin();
        httpClient.toBlocking().exchange(HttpRequest.POST("/stock/book/reserve?quantity=3", "")
            .header("Link", link(gtrid)));

        assertEquals(200, coordinator("DELETE", gtrid).statusCode()); // <8>
        assertEquals(10, stock.findById("book").orElseThrow().quantity());
    }

    @Test
    void reservingMoreThanTheStockFails() throws Exception {
        String gtrid = begin();

        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
            httpClient.toBlocking().exchange(HttpRequest.POST("/stock/book/reserve?quantity=30", "")
                .header("Link", link(gtrid))));
        assertEquals(HttpStatus.CONFLICT, e.getStatus());

        coordinator("DELETE", gtrid);
        assertEquals(10, stock.findById("book").orElseThrow().quantity());
    }

    private static String begin() throws Exception {
        HttpResponse<String> response = coordinator("POST", null);
        Matcher matcher = Pattern.compile("xa-transaction/([^\"]+)\"").matcher(response.body());
        matcher.find();
        return matcher.group(1);
    }

    private static String link(String gtrid) {
        return "<http://localhost:9000/api/v1/xa-transaction/" + gtrid + ">; rel=\"https://otmm.oracle.com/xa-transaction/internal\"";
    }

    private static HttpResponse<String> coordinator(String method, String gtrid) throws Exception {
        String url = coordinatorUrl() + "/xa-transaction" + (gtrid == null ? "" : "/" + gtrid);
        try (java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient()) {
            return client.send(java.net.http.HttpRequest.newBuilder(URI.create(url))
                .method(method, java.net.http.HttpRequest.BodyPublishers.noBody()).build(), HttpResponse.BodyHandlers.ofString());
        }
    }

    private static String coordinatorUrl() {
        return "http://" + COORDINATOR.getHost() + ":" + COORDINATOR.getMappedPort(9000) + "/api/v1";
    }

    private static int freePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
