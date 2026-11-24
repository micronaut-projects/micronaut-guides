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

import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.oracledb.microtx.tcc.MicroTxTccClient
import io.micronaut.oracledb.microtx.tcc.TCC
import java.net.URI

@Controller("/bookings") // <1>
class BookingController(
    private val bookings: BookingRepository,
    private val tcc: MicroTxTccClient, // <2>
    @Value("\${hotel.url}") private val hotelUrl: String // <3>
) {

    @TCC(timeLimit = 60) // <4>
    @Post
    fun reserve(@Body request: BookingRequest): HttpResponse<Booking> {
        val booking = bookings.reserve(request.hotel) // <5>
        tcc.transaction.addParticipant(URI.create("$hotelUrl/bookings/${booking.id}")) // <6>
        return HttpResponse.created(booking)
    }

    @Put("/{id}") // <7>
    fun confirm(id: String): Booking? = bookings.update(id, BookingStatus.CONFIRMED)

    @Delete("/{id}") // <8>
    fun cancel(id: String): Booking? = bookings.update(id, BookingStatus.CANCELLED)

    @Get("/{id}")
    fun find(id: String): Booking? = bookings.find(id)
}
