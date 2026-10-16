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

import groovy.transform.CompileStatic

import example.micronaut.dto.BookingRequest
import example.micronaut.dto.BookingView
import example.micronaut.dto.FlightSeatView
import example.micronaut.dto.PaymentRequested
import example.micronaut.entity.Booking
import example.micronaut.kafka.BookingClient
import example.micronaut.repository.BookingRepository
import example.micronaut.repository.FlightSeatRepository
import example.micronaut.service.BookingService

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.transaction.jdbc.oracle.OracleSessionlessTransactionPropagationOperations
import jakarta.validation.Valid
import java.util.Optional

@CompileStatic
@Controller("/bookings") // <1>
@ExecuteOn(TaskExecutors.BLOCKING) // <2>
class BookingController {
    private final BookingClient bookingClient
    private final BookingService bookingService
    private final OracleSessionlessTransactionPropagationOperations propagation
    private final BookingRepository bookingRepository
    private final FlightSeatRepository flightSeatRepository

    BookingController(BookingClient bookingClient,
                             BookingService bookingService,
                             OracleSessionlessTransactionPropagationOperations propagation,
                             BookingRepository bookingRepository,
                             FlightSeatRepository flightSeatRepository) {
        this.bookingClient = bookingClient
        this.bookingService = bookingService
        this.propagation = propagation
        this.bookingRepository = bookingRepository
        this.flightSeatRepository = flightSeatRepository
    }

    @Serdeable // <10>
    @groovy.transform.Canonical
    static class AcceptedBooking {
        Long bookingId
    }

    @Post // <3>
    HttpResponse<AcceptedBooking> book(@Body @Valid BookingRequest request) {
        PaymentRequested payment = propagation.withPropagation({ -> // <4>
            Booking booking = bookingService.reserve(request.seatId) // <5>
            String transactionId = propagation.currentTransactionId().orElseThrow() // <6>
            return new PaymentRequested(booking.getId(), transactionId, booking.getAmount(), request.paymentToken)
        })
        bookingClient.requestPayment(payment.bookingId, payment) // <7>
        return HttpResponse.accepted().body(new AcceptedBooking(payment.bookingId))
    }

    @Get("/{id}") // <8>
    Optional<BookingView> find(Long id) {
        return bookingRepository.queryById(id)
    }

    @Get("/seats/{id}") // <9>
    Optional<FlightSeatView> seat(String id) {
        return flightSeatRepository.queryById(id)
    }
}
