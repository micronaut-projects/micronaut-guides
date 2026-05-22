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

import io.micronaut.data.model.geo.Point
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@MicronautTest(transactional = false) // <1>
class DeliveryDispatchControllerTest(
    private val deliveryDriverRepository: DeliveryDriverRepository,
    @Client("/") private val httpClient: HttpClient // <2>
) {

    @BeforeEach
    fun clean() {
        deliveryDriverRepository.deleteAll()
    }

    @Test
    fun findsClosestAvailableDriverWithinFiveKilometers() {
        deliveryDriverRepository.save(
            DeliveryDriver(
                "Nearby Driver",
                DeliveryDriver.AVAILABLE,
                Point(-73.9757, 40.7554)
            )
        )
        val closest = deliveryDriverRepository.save(
            DeliveryDriver(
                "Closest Driver",
                DeliveryDriver.AVAILABLE,
                Point(-73.9827, 40.7504)
            )
        )
        deliveryDriverRepository.save(
            DeliveryDriver(
                "Busy Driver",
                DeliveryDriver.BUSY,
                Point(-73.9850, 40.7488)
            )
        ) // <3>
        deliveryDriverRepository.save(
            DeliveryDriver(
                "Far Driver",
                DeliveryDriver.AVAILABLE,
                Point(-73.9000, 40.8000)
            )
        ) // <4>

        val client = httpClient.toBlocking()
        val response: HttpResponse<DriverMatch> = client.exchange(
            HttpRequest.GET<Any>("/orders/nearest-driver?longitude=-73.9857&latitude=40.7484"),
            DriverMatch::class.java
        ) // <5>

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(closest.id, response.body()!!.driverId) // <6>
    }

    @Test
    fun returnsNotFoundWhenNoAvailableDriverIsCloseEnough() {
        deliveryDriverRepository.save(
            DeliveryDriver(
                "Busy Driver",
                DeliveryDriver.BUSY,
                Point(-73.9850, 40.7488)
            )
        )
        deliveryDriverRepository.save(
            DeliveryDriver(
                "Far Driver",
                DeliveryDriver.AVAILABLE,
                Point(-73.9000, 40.8000)
            )
        )

        val client = httpClient.toBlocking()
        val thrown = assertThrows<HttpClientResponseException> {
            client.exchange(
                HttpRequest.GET<Any>("/orders/nearest-driver?longitude=-73.9857&latitude=40.7484"),
                DriverMatch::class.java
            )
        } // <7>

        assertEquals(HttpStatus.NOT_FOUND, thrown.status)
    }
}
