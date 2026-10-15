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
package example.micronaut.service;

import example.micronaut.dto.PaymentCompleted;
import example.micronaut.entity.Booking;
import example.micronaut.entity.BookingStatus;
import example.micronaut.entity.FlightSeat;
import example.micronaut.entity.SeatStatus;
import example.micronaut.repository.BookingRepository;
import example.micronaut.repository.FlightSeatRepository;

import io.micronaut.transaction.annotation.OracleTransactional;
import jakarta.inject.Singleton;
import java.math.BigDecimal;

import static io.micronaut.transaction.annotation.OracleTransactional.Sessionless.REQUIRES_SUSPENDED;
import static io.micronaut.transaction.annotation.OracleTransactional.Sessionless.SUSPEND;

@Singleton // <9>
public class BookingService {
    private final FlightSeatRepository flightSeatRepository;
    private final BookingRepository bookingRepository;

    public BookingService(FlightSeatRepository flightSeatRepository, BookingRepository bookingRepository) {
        this.flightSeatRepository = flightSeatRepository;
        this.bookingRepository = bookingRepository;
    }

    @OracleTransactional(sessionless = SUSPEND, timeout = 300) // <1>
    public Booking reserve(String seatId) {
        FlightSeat seat = flightSeatRepository.findByIdForUpdate(seatId).orElseThrow(); // <2>
        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new IllegalStateException("Seat is unavailable: " + seatId);
        }
        seat.setStatus(SeatStatus.HELD);
        flightSeatRepository.update(seat);
        BigDecimal amount = new BigDecimal("199.00");
        return bookingRepository.save(new Booking(null, seatId, amount, BookingStatus.PENDING, "NONE")); // <3>
    }

    @OracleTransactional(sessionless = REQUIRES_SUSPENDED) // <4>
    public void complete(PaymentCompleted result) {
        Booking booking = bookingRepository.findByIdAndStatus(result.bookingId(), BookingStatus.PENDING).orElseThrow(); // <5>
        if (!result.approved()) {
            throw new PaymentDeclinedException(); // <6>
        }
        FlightSeat seat = flightSeatRepository.findByIdAndStatus(booking.seatId(), SeatStatus.HELD).orElseThrow(); // <7>
        seat.setStatus(SeatStatus.BOOKED);
        flightSeatRepository.update(seat);
        bookingRepository.update(new Booking(booking.id(), booking.seatId(), booking.amount(),
            BookingStatus.CONFIRMED, result.paymentReference()));
    } // <8>
}
