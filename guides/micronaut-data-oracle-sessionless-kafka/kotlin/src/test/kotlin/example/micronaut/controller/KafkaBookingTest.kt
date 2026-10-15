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
package example.micronaut.controller

import example.micronaut.dto.BookingRequest
import example.micronaut.dto.BookingView
import example.micronaut.dto.FlightSeatView
import example.micronaut.entity.BookingStatus
import example.micronaut.entity.SeatStatus
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.time.Duration
import java.util.Optional

@MicronautTest(transactional = false) // <1>
@Property(name = "micronaut.test.resources.scope", value = "booking-controller-e2e") // <2>
@Property(name = "micronaut.http.client.read-timeout", value = "30s")
@Sql(scripts = ["classpath:rollbackSeed.sql"], phase = Sql.Phase.AFTER_ALL)
@Timeout(120)
class KafkaBookingTest {
    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun approvedPaymentConfirmsTheBooking() {
        val seatId = "MN123-12A"
        assertEquals(SeatStatus.AVAILABLE, findSeat(seatId).status)
        val bookingId = book(seatId, "demo-approve")
        awaitConfirmed(bookingId, seatId)
    }

    @Test
    fun declinedPaymentRollsBackAndAllowsTheSameSeatToBeBookedAgain() {
        val seatId = "MN123-12B"
        assertEquals(SeatStatus.AVAILABLE, findSeat(seatId).status)
        val declinedBookingId = book(seatId, "demo-decline") // <1>
        val approvedBookingId = book(seatId, "demo-approve") // <2>
        assertNotEquals(declinedBookingId, approvedBookingId)
        awaitConfirmed(approvedBookingId, seatId) // <3>
        assertTrue(findBooking(declinedBookingId).isEmpty) // <4>
    }

    private fun book(seatId: String, paymentToken: String): Long {
        val response = httpClient.toBlocking().exchange(
            HttpRequest.POST("/bookings", BookingRequest(seatId, paymentToken)),
            BookingController.AcceptedBooking::class.java
        )
        assertEquals(HttpStatus.ACCEPTED, response.status)
        val bookingId = requireNotNull(response.body()).bookingId
        assertTrue(bookingId > 0)
        return bookingId
    }

    private fun awaitConfirmed(bookingId: Long, seatId: String) {
        await().atMost(Duration.ofSeconds(60)).untilAsserted {
            val booking = findBooking(bookingId)
            assertTrue(booking.isPresent, "Booking has not committed yet")
            val view = booking.orElseThrow()
            assertEquals(bookingId, view.id)
            assertEquals(seatId, view.seatId)
            assertEquals(BookingStatus.CONFIRMED, view.status)
            assertEquals("demo-$bookingId", view.paymentReference)
            assertEquals(SeatStatus.BOOKED, findSeat(seatId).status)
        }
    }

    private fun findBooking(bookingId: Long): Optional<BookingView> = try {
        Optional.of(httpClient.toBlocking().retrieve("/bookings/$bookingId", BookingView::class.java))
    } catch (e: HttpClientResponseException) {
        if (e.status != HttpStatus.NOT_FOUND) throw e
        Optional.empty()
    }

    private fun findSeat(seatId: String): FlightSeatView =
        httpClient.toBlocking().retrieve("/bookings/seats/$seatId", FlightSeatView::class.java)
}
