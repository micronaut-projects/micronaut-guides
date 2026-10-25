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
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private const val TRANSACTION_LINK = "<http://localhost:9000/api/v1/tcc-transaction/a1b2c3>; rel=\"https://otmm.oracle.com/tcc-transaction\""

@MicronautTest
class BookingControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun aReservationJoinsTheTransactionOfTheCaller() {
        val client = httpClient.toBlocking()

        val response = client.exchange(HttpRequest.POST("/bookings", BookingRequest("Grand"))
            .header("Link", TRANSACTION_LINK), Booking::class.java) // <1>

        assertEquals(HttpStatus.CREATED, response.status)
        val booking = response.body()!!
        assertEquals(BookingStatus.PROVISIONAL, booking.status)
        assertEquals("<http://localhost:8081/bookings/${booking.id}>; rel=\"https://otmm.oracle.com/tcc-participant\"; timeLimit=60000",
            response.header("Link")) // <2>

        assertEquals(BookingStatus.CONFIRMED, client.retrieve(HttpRequest.PUT("/bookings/${booking.id}", ""), Booking::class.java).status) // <3>
    }

    @Test
    fun theCoordinatorCancelsAReservation() {
        val client = httpClient.toBlocking()
        val booking = client.retrieve(HttpRequest.POST("/bookings", BookingRequest("Grand"))
            .header("Link", TRANSACTION_LINK), Booking::class.java)

        assertEquals(BookingStatus.CANCELLED, client.retrieve(HttpRequest.DELETE<Any>("/bookings/${booking.id}"), Booking::class.java).status)
    }
}
