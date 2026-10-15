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
import example.micronaut.entity.FlightSeat
import example.micronaut.entity.SeatStatus
import example.micronaut.repository.BookingRepository
import example.micronaut.repository.FlightSeatRepository
import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import spock.lang.Timeout

import java.time.Duration
import static org.awaitility.Awaitility.await

@MicronautTest(transactional = false) // <1>
@Property(name = "micronaut.test.resources.scope", value = "booking-controller-e2e") // <2>
@Property(name = "micronaut.http.client.read-timeout", value = "30s")
@Timeout(120)
class KafkaBookingSpec extends Specification {
    @Inject
    @Client("/")
    HttpClient httpClient

    @Inject
    BookingRepository bookingRepository

    @Inject
    FlightSeatRepository flightSeatRepository

    void cleanup() { // <3>
        bookingRepository.deleteAll()
        flightSeatRepository.update(new FlightSeat("MN123-12A", SeatStatus.AVAILABLE))
        flightSeatRepository.update(new FlightSeat("MN123-12B", SeatStatus.AVAILABLE))
    }

    void "approved payment confirms the booking"() {
        given:
        String seatId = "MN123-12A"
        assert findSeat(seatId).status == SeatStatus.AVAILABLE

        when:
        Long bookingId = book(seatId, "demo-approve")

        then:
        awaitConfirmed(bookingId, seatId)
    }

    void "declined payment rolls back and allows the same seat to be booked again"() {
        given:
        String seatId = "MN123-12B"
        assert findSeat(seatId).status == SeatStatus.AVAILABLE

        when:
        Long declinedBookingId = book(seatId, "demo-decline") // <4>
        Long approvedBookingId = book(seatId, "demo-approve") // <5>

        then:
        declinedBookingId != approvedBookingId
        awaitConfirmed(approvedBookingId, seatId) // <6>
        findBooking(declinedBookingId).empty // <7>
    }

    private Long book(String seatId, String paymentToken) {
        def response = httpClient.toBlocking().exchange(
            HttpRequest.POST("/bookings", new BookingRequest(seatId, paymentToken)),
            BookingController.AcceptedBooking)
        assert response.status == HttpStatus.ACCEPTED
        Long bookingId = response.body().bookingId
        assert bookingId != null && bookingId > 0
        bookingId
    }

    private boolean awaitConfirmed(Long bookingId, String seatId) {
        await().atMost(Duration.ofSeconds(60)).untilAsserted {
            Optional<BookingView> booking = findBooking(bookingId)
            assert booking.present : "Booking has not committed yet"
            BookingView view = booking.orElseThrow()
            assert view.id == bookingId
            assert view.seatId == seatId
            assert view.status == BookingStatus.CONFIRMED
            assert view.paymentReference == "demo-" + bookingId
            assert findSeat(seatId).status == SeatStatus.BOOKED
        }
        true
    }

    private Optional<BookingView> findBooking(Long bookingId) {
        try {
            Optional.of(httpClient.toBlocking().retrieve("/bookings/" + bookingId, BookingView))
        } catch (HttpClientResponseException e) {
            if (e.status != HttpStatus.NOT_FOUND) throw e
            Optional.empty()
        }
    }

    private FlightSeatView findSeat(String seatId) {
        httpClient.toBlocking().retrieve("/bookings/seats/" + seatId, FlightSeatView)
    }
}
