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

@Controller("/bookings") // <1>
class BookingController {

    private final BookingRepository bookings
    private final MicroTxTccClient tcc
    private final String hotelUrl

    BookingController(BookingRepository bookings,
                      MicroTxTccClient tcc, // <2>
                      @Value('${hotel.url}') String hotelUrl) { // <3>
        this.bookings = bookings
        this.tcc = tcc
        this.hotelUrl = hotelUrl
    }

    @TCC(timeLimit = 60L) // <4>
    @Post
    HttpResponse<Booking> reserve(@Body BookingRequest request) {
        Booking booking = bookings.reserve(request.hotel) // <5>
        tcc.transaction.addParticipant(URI.create("${hotelUrl}/bookings/${booking.id}")) // <6>
        HttpResponse.created(booking)
    }

    @Put("/{id}") // <7>
    Optional<Booking> confirm(String id) {
        bookings.update(id, BookingStatus.CONFIRMED)
    }

    @Delete("/{id}") // <8>
    Optional<Booking> cancel(String id) {
        bookings.update(id, BookingStatus.CANCELLED)
    }

    @Get("/{id}")
    Optional<Booking> find(String id) {
        bookings.find(id)
    }
}
