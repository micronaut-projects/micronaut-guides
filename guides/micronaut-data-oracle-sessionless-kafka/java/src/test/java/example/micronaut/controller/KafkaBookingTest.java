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
package example.micronaut.controller;

import example.micronaut.dto.BookingRequest;
import example.micronaut.dto.BookingView;
import example.micronaut.dto.FlightSeatView;
import example.micronaut.entity.BookingStatus;
import example.micronaut.entity.SeatStatus;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.Optional;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
@Property(name = "micronaut.test.resources.scope", value = "booking-controller-e2e")
@Property(name = "micronaut.http.client.read-timeout", value = "30s")
@Timeout(120)
class KafkaBookingTest {
    @Inject @Client("/") HttpClient httpClient;

    @Test
    void approvedPaymentConfirmsTheBooking() {
        String seatId = "MN123-12A";
        assertEquals(SeatStatus.AVAILABLE, findSeat(seatId).status());

        Long bookingId = book(seatId, "demo-approve");
        awaitConfirmed(bookingId, seatId);
    }

    @Test
    void declinedPaymentRollsBackAndAllowsTheSameSeatToBeBookedAgain() {
        String seatId = "MN123-12B";
        assertEquals(SeatStatus.AVAILABLE, findSeat(seatId).status());

        Long declinedBookingId = book(seatId, "demo-decline"); // <1>
        Long approvedBookingId = book(seatId, "demo-approve"); // <2>
        assertNotEquals(declinedBookingId, approvedBookingId);
        awaitConfirmed(approvedBookingId, seatId); // <3>
        assertTrue(findBooking(declinedBookingId).isEmpty()); // <4>
    }

    private Long book(String seatId, String paymentToken) {
        var response = httpClient.toBlocking().exchange(
            HttpRequest.POST("/bookings", new BookingRequest(seatId, paymentToken)),
            BookingController.AcceptedBooking.class);
        assertEquals(HttpStatus.ACCEPTED, response.getStatus());
        Long bookingId = response.body().bookingId();
        assertNotNull(bookingId);
        assertTrue(bookingId > 0);
        return bookingId;
    }

    private void awaitConfirmed(Long bookingId, String seatId) {
        await().atMost(Duration.ofSeconds(60)).untilAsserted(() -> {
            Optional<BookingView> booking = findBooking(bookingId);
            assertTrue(booking.isPresent(), "Booking has not committed yet");
            BookingView view = booking.orElseThrow();
            assertEquals(bookingId, view.id());
            assertEquals(seatId, view.seatId());
            assertEquals(BookingStatus.CONFIRMED, view.status());
            assertEquals("demo-" + bookingId, view.paymentReference());
            assertEquals(SeatStatus.BOOKED, findSeat(seatId).status());
        });
    }

    private Optional<BookingView> findBooking(Long bookingId) {
        try {
            return Optional.of(httpClient.toBlocking().retrieve("/bookings/" + bookingId, BookingView.class));
        } catch (HttpClientResponseException e) {
            if (e.getStatus() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw e;
        }
    }

    private FlightSeatView findSeat(String seatId) {
        return httpClient.toBlocking().retrieve("/bookings/seats/" + seatId, FlightSeatView.class);
    }
}
