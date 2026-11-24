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
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.Testcontainers
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.Transferable
import java.net.ServerSocket

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TripControllerTest : TestPropertyProvider { // <1>

    private val port = ServerSocket(0).use { it.localPort }

    private val coordinator = GenericContainer("container-registry.oracle.com/database/otmm:24.4.1") // <2>
        .withCopyToContainer(Transferable.of("""
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
            """.trimIndent()), "/app/config/tcs.yaml")
        .withEnv("CONFIG_FILE", "/app/config/tcs.yaml")
        .withEnv("SERVE_TLS_ENABLED", "false")
        .withExposedPorts(9000)
        .waitingFor(Wait.forHttp("/health"))

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var hotel: HotelStub

    override fun getProperties(): Map<String, String> {
        Testcontainers.exposeHostPorts(port) // <3>
        coordinator.start()
        return mapOf(
            "micronaut.server.port" to port.toString(),
            "microtx.coordinator-url" to "http://${coordinator.host}:${coordinator.getMappedPort(9000)}/api/v1",
            "micronaut.http.services.hotel.url" to "http://localhost:$port",
            "hotel.stub-url" to "http://host.testcontainers.internal:$port" // <4>
        )
    }

    @Test
    fun confirmingATripConfirmsTheHotelBooking() {
        val result = httpClient.toBlocking().retrieve(HttpRequest.POST("/trips", TripRequest("grand", true)), TccResult::class.java)

        assertEquals("Confirmed", result.status()) // <5>
        assertEquals("confirmed", hotel.bookings["grand"]) // <6>
    }

    @Test
    fun cancellingATripCancelsTheHotelBooking() {
        val result = httpClient.toBlocking().retrieve(HttpRequest.POST("/trips", TripRequest("palace", false)), TccResult::class.java)

        assertEquals("Cancelled", result.status())
        assertEquals("cancelled", hotel.bookings["palace"])
    }
}
