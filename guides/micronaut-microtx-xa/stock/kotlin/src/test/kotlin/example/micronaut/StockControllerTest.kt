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
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.Testcontainers
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.Transferable
import java.net.ServerSocket
import java.net.URI
import java.net.http.HttpResponse

@MicronautTest(transactional = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StockControllerTest : TestPropertyProvider {

    private val port = ServerSocket(0).use { it.localPort }

    private val coordinator = GenericContainer("container-registry.oracle.com/database/otmm:24.4.1") // <1>
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
            """.trimIndent()), "/app/config/tcs.yaml")
        .withEnv("CONFIG_FILE", "/app/config/tcs.yaml")
        .withEnv("SERVE_TLS_ENABLED", "false")
        .withExposedPorts(9000)
        .waitingFor(Wait.forHttp("/health"))

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var stock: StockRepository

    override fun getProperties(): Map<String, String> {
        Testcontainers.exposeHostPorts(port) // <2>
        coordinator.start()
        return mapOf(
            "micronaut.server.port" to port.toString(),
            "microtx.coordinator-url" to coordinatorUrl(),
            "microtx.xa.participant-url" to "http://host.testcontainers.internal:$port" // <3>
        )
    }

    @BeforeEach
    fun stock() {
        stock.deleteAll()
        stock.save(Stock("book", 10))
    }

    @Test
    fun aCommittedReservationReducesTheStock() {
        val gtrid = begin() // <4>

        httpClient.toBlocking().exchange<String, Any>(HttpRequest.POST("/stock/book/reserve?quantity=3", "")
            .header("Link", link(gtrid))) // <5>
        assertEquals(10, stock.findById("book").get().quantity) // <6>

        assertEquals(200, coordinator("PUT", gtrid).statusCode()) // <7>
        assertEquals(7, stock.findById("book").get().quantity)
    }

    @Test
    fun aRolledBackReservationKeepsTheStock() {
        val gtrid = begin()
        httpClient.toBlocking().exchange<String, Any>(HttpRequest.POST("/stock/book/reserve?quantity=3", "")
            .header("Link", link(gtrid)))

        assertEquals(200, coordinator("DELETE", gtrid).statusCode()) // <8>
        assertEquals(10, stock.findById("book").get().quantity)
    }

    @Test
    fun reservingMoreThanTheStockFails() {
        val gtrid = begin()

        val e = assertThrows(HttpClientResponseException::class.java) {
            httpClient.toBlocking().exchange<String, Any>(HttpRequest.POST("/stock/book/reserve?quantity=30", "")
                .header("Link", link(gtrid)))
        }
        assertEquals(HttpStatus.CONFLICT, e.status)

        coordinator("DELETE", gtrid)
        assertEquals(10, stock.findById("book").get().quantity)
    }

    private fun begin(): String =
        Regex("xa-transaction/([^\"]+)\"").find(coordinator("POST", null).body())!!.groupValues[1]

    private fun link(gtrid: String) =
        "<http://localhost:9000/api/v1/xa-transaction/$gtrid>; rel=\"https://otmm.oracle.com/xa-transaction/internal\""

    private fun coordinator(method: String, gtrid: String?): HttpResponse<String> {
        val url = coordinatorUrl() + "/xa-transaction" + (if (gtrid == null) "" else "/$gtrid")
        return java.net.http.HttpClient.newHttpClient().use { client ->
            client.send(java.net.http.HttpRequest.newBuilder(URI.create(url))
                .method(method, java.net.http.HttpRequest.BodyPublishers.noBody()).build(), HttpResponse.BodyHandlers.ofString())
        }
    }

    private fun coordinatorUrl() = "http://${coordinator.host}:${coordinator.getMappedPort(9000)}/api/v1"
}
