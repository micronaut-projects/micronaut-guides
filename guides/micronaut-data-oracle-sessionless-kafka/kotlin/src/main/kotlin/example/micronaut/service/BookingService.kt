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
package example.micronaut.service

import example.micronaut.dto.PaymentCompleted
import example.micronaut.entity.Booking
import example.micronaut.entity.BookingStatus
import example.micronaut.entity.SeatStatus
import example.micronaut.repository.BookingRepository
import example.micronaut.repository.FlightSeatRepository
import io.micronaut.transaction.annotation.OracleTransactional
import io.micronaut.transaction.annotation.OracleTransactional.Sessionless.REQUIRES_SUSPENDED
import io.micronaut.transaction.annotation.OracleTransactional.Sessionless.SUSPEND
import jakarta.inject.Singleton
import java.math.BigDecimal

@Singleton // <9>
open class BookingService(
    private val flightSeatRepository: FlightSeatRepository,
    private val bookingRepository: BookingRepository
) {
    @OracleTransactional(sessionless = SUSPEND, timeout = 300) // <1>
    open fun reserve(seatId: String): Booking {
        val seat = flightSeatRepository.findByIdForUpdate(seatId).orElseThrow() // <2>
        check(seat.status == SeatStatus.AVAILABLE) { "Seat is unavailable: $seatId" }
        seat.status = SeatStatus.HELD
        flightSeatRepository.update(seat)
        val amount = BigDecimal("199.00")
        return bookingRepository.save(Booking(seatId, amount, BookingStatus.PENDING, "NONE")) // <3>
    }

    @OracleTransactional(sessionless = REQUIRES_SUSPENDED) // <4>
    open fun complete(result: PaymentCompleted) {
        val booking = bookingRepository.findByIdAndStatus(result.bookingId, BookingStatus.PENDING).orElseThrow() // <5>
        if (!result.approved) {
            throw PaymentDeclinedException() // <6>
        }
        val seat = flightSeatRepository.findByIdAndStatus(booking.seatId, SeatStatus.HELD).orElseThrow() // <7>
        seat.status = SeatStatus.BOOKED
        flightSeatRepository.update(seat)
        booking.status = BookingStatus.CONFIRMED
        booking.paymentReference = result.paymentReference
        bookingRepository.update(booking)
    } // <8>
}
