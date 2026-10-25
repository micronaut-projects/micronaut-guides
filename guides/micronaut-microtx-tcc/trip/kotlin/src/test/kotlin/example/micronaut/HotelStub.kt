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

import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.oracledb.microtx.tcc.MicroTxTccClient
import io.micronaut.oracledb.microtx.tcc.TCC
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

/**
 * A hotel service that runs in the test, at an address the coordinator can call.
 */
@Requires(property = "hotel.stub-url")
@Controller("/bookings")
class HotelStub(
    private val tcc: MicroTxTccClient,
    @Value("\${hotel.stub-url}") private val stubUrl: String
) {

    val bookings = ConcurrentHashMap<String, String>()

    @TCC(timeLimit = 60)
    @Post
    fun reserve(@Body request: BookingRequest): HttpResponse<*> {
        bookings[request.hotel] = "provisional"
        tcc.transaction.addParticipant(URI.create("$stubUrl/bookings/${request.hotel}"))
        return HttpResponse.status<Any>(HttpStatus.CREATED)
    }

    @Put("/{hotel}")
    fun confirm(hotel: String) {
        bookings[hotel] = "confirmed"
    }

    @Delete("/{hotel}")
    fun cancel(hotel: String) {
        bookings[hotel] = "cancelled"
    }
}
