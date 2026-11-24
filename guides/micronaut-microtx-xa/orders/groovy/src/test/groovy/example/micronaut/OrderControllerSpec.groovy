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
import io.micronaut.http.HttpResponse
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

@MicronautTest(transactional = false)
class OrderControllerSpec extends Specification implements TestPropertyProvider {

    @Shared
    int port = new ServerSocket(0).withCloseable { it.localPort }

    @Shared
    GenericContainer<?> coordinator = new GenericContainer<>("container-registry.oracle.com/database/otmm:24.4.1")
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
    PurchaseOrderRepository orders

    @Override
    Map<String, String> getProperties() {
        Testcontainers.exposeHostPorts(port)
        coordinator.start()
        [
            "micronaut.server.port"            : String.valueOf(port),
            "microtx.coordinator-url"          : "http://${coordinator.host}:${coordinator.getMappedPort(9000)}/api/v1".toString(),
            "microtx.xa.participant-url"       : "http://host.testcontainers.internal:${port}".toString(),
            "micronaut.http.services.stock.url": "http://localhost:${port}".toString(),
            "stock.stub"                       : "true" // <1>
        ]
    }

    def setup() {
        orders.deleteAll()
    }

    void "an order commits when the stock is reserved"() {
        when:
        HttpResponse<PurchaseOrder> response = httpClient.toBlocking().exchange(
            HttpRequest.POST("/orders", new OrderRequest("book", 2)), PurchaseOrder)

        then:
        response.status == HttpStatus.CREATED
        orders.count() == 1 // <2>
    }

    void "an order rolls back when the stock cannot be reserved"() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.POST("/orders", new OrderRequest("book", 20)), PurchaseOrder)

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.INTERNAL_SERVER_ERROR
        orders.count() == 0 // <3>
    }
}
