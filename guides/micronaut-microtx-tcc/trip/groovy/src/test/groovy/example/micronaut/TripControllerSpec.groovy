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
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.oracledb.microtx.tcc.TccResult
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.Testcontainers
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.Transferable
import spock.lang.Shared
import spock.lang.Specification

@MicronautTest
class TripControllerSpec extends Specification implements TestPropertyProvider { // <1>

    @Shared
    int port = new ServerSocket(0).withCloseable { it.localPort }

    @Shared
    GenericContainer<?> coordinator = new GenericContainer<>("container-registry.oracle.com/database/otmm:24.4.1") // <2>
        .withCopyToContainer(Transferable.of('''
tmmConfiguration:
  tmmAppName: tcs
  listenAddr: 0.0.0.0:9000
  internalAddr: http://localhost:9000
  externalUrl: http://localhost:9000
  tccCoordinator:
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
    HotelStub hotel

    @Override
    Map<String, String> getProperties() {
        Testcontainers.exposeHostPorts(port) // <3>
        coordinator.start()
        [
            "micronaut.server.port"            : String.valueOf(port),
            "microtx.coordinator-url"          : "http://${coordinator.host}:${coordinator.getMappedPort(9000)}/api/v1".toString(),
            "micronaut.http.services.hotel.url": "http://localhost:${port}".toString(),
            "hotel.stub-url"                   : "http://host.testcontainers.internal:${port}".toString() // <4>
        ]
    }

    void "confirming a trip confirms the hotel booking"() {
        when:
        TccResult result = httpClient.toBlocking().retrieve(HttpRequest.POST("/trips", new TripRequest("grand", true)), TccResult)

        then:
        result.status() == "Confirmed" // <5>
        hotel.bookings["grand"] == "confirmed" // <6>
    }

    void "cancelling a trip cancels the hotel booking"() {
        when:
        TccResult result = httpClient.toBlocking().retrieve(HttpRequest.POST("/trips", new TripRequest("palace", false)), TccResult)

        then:
        result.status() == "Cancelled"
        hotel.bookings["palace"] == "cancelled"
    }
}
