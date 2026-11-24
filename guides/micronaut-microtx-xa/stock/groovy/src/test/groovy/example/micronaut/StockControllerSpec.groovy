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
package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.Testcontainers
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.Transferable
import spock.lang.Shared
import spock.lang.Specification

import java.net.http.HttpResponse

@MicronautTest(transactional = false)
class StockControllerSpec extends Specification implements TestPropertyProvider {

    @Shared
    int port = new ServerSocket(0).withCloseable { it.localPort }

    @Shared
    GenericContainer<?> coordinator = new GenericContainer<>("container-registry.oracle.com/database/otmm:24.4.1") // <1>
        .withCopyToContainer(Transferable.of('''
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
'''), "/app/config/tcs.yaml")
        .withEnv("CONFIG_FILE", "/app/config/tcs.yaml")
        .withEnv("SERVE_TLS_ENABLED", "false")
        .withExposedPorts(9000)
        .waitingFor(Wait.forHttp("/health"))

    @Inject
    @Client("/")
    HttpClient httpClient

    @Inject
    StockRepository stock

    @Override
    Map<String, String> getProperties() {
        Testcontainers.exposeHostPorts(port) // <2>
        coordinator.start()
        [
            "micronaut.server.port"     : String.valueOf(port),
            "microtx.coordinator-url"   : coordinatorUrl(),
            "microtx.xa.participant-url": "http://host.testcontainers.internal:${port}".toString() // <3>
        ]
    }

    def setup() {
        stock.deleteAll()
        stock.save(new Stock(item: "book", quantity: 10))
    }

    void "a committed reservation reduces the stock"() {
        given:
        String gtrid = begin() // <4>

        when:
        httpClient.toBlocking().exchange(HttpRequest.POST("/stock/book/reserve?quantity=3", "")
            .header("Link", link(gtrid))) // <5>

        then:
        stock.findById("book").get().quantity == 10 // <6>

        when:
        int status = coordinator("PUT", gtrid).statusCode() // <7>

        then:
        status == 200
        stock.findById("book").get().quantity == 7
    }

    void "a rolled back reservation keeps the stock"() {
        given:
        String gtrid = begin()
        httpClient.toBlocking().exchange(HttpRequest.POST("/stock/book/reserve?quantity=3", "")
            .header("Link", link(gtrid)))

        expect:
        coordinator("DELETE", gtrid).statusCode() == 200 // <8>
        stock.findById("book").get().quantity == 10
    }

    void "reserving more than the stock fails"() {
        given:
        String gtrid = begin()

        when:
        httpClient.toBlocking().exchange(HttpRequest.POST("/stock/book/reserve?quantity=30", "")
            .header("Link", link(gtrid)))

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.CONFLICT

        when:
        coordinator("DELETE", gtrid)

        then:
        stock.findById("book").get().quantity == 10
    }

    private String begin() {
        def matcher = coordinator("POST", null).body() =~ /xa-transaction\/([^"]+)"/
        matcher.find()
        matcher.group(1)
    }

    private static String link(String gtrid) {
        "<http://localhost:9000/api/v1/xa-transaction/${gtrid}>; rel=\"https://otmm.oracle.com/xa-transaction/internal\""
    }

    private HttpResponse<String> coordinator(String method, String gtrid) {
        String url = coordinatorUrl() + "/xa-transaction" + (gtrid == null ? "" : "/${gtrid}")
        java.net.http.HttpClient.newHttpClient().withCloseable { client ->
            client.send(java.net.http.HttpRequest.newBuilder(URI.create(url))
                .method(method, java.net.http.HttpRequest.BodyPublishers.noBody()).build(), HttpResponse.BodyHandlers.ofString())
        }
    }

    private String coordinatorUrl() {
        "http://${coordinator.host}:${coordinator.getMappedPort(9000)}/api/v1"
    }
}
