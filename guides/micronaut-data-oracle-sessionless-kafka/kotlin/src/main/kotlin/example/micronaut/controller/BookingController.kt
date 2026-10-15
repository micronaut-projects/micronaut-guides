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
import example.micronaut.dto.PaymentRequested
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

@Controller("/bookings")
@ExecuteOn(TaskExecutors.BLOCKING)
open class BookingController(
    private val bookingClient: BookingClient,
    private val bookingService: BookingService,
    private val propagation: OracleSessionlessTransactionPropagationOperations,
    private val bookingRepository: BookingRepository,
    private val flightSeatRepository: FlightSeatRepository
) {
    @Serdeable // <5>
    data class AcceptedBooking(val bookingId: Long)

    @Post
    open fun book(@Body @Valid request: BookingRequest): HttpResponse<AcceptedBooking> {
        val payment = propagation.withPropagation { // <1>
            val booking = bookingService.reserve(request.seatId) // <2>
            val transactionId = propagation.currentTransactionId().orElseThrow() // <3>
            PaymentRequested(requireNotNull(booking.id), transactionId, booking.amount, request.paymentToken)
        }
        bookingClient.requestPayment(payment.bookingId, payment) // <4>
        return HttpResponse.accepted<AcceptedBooking>().body(AcceptedBooking(payment.bookingId))
    }

    @Get("/{id}")
    open fun find(id: Long): Optional<BookingView> = bookingRepository.findById(id).map(BookingView::from)

    @Get("/seats/{id}")
    open fun seat(id: String): Optional<FlightSeatView> = flightSeatRepository.findById(id).map(FlightSeatView::from)
}
