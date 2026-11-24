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

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class BookingControllerSpec extends Specification {

    static final String TRANSACTION_LINK = '<http://localhost:9000/api/v1/tcc-transaction/a1b2c3>; rel="https://otmm.oracle.com/tcc-transaction"'

    @Inject
    @Client("/")
    HttpClient httpClient

    void "a reservation joins the transaction of the caller"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        HttpResponse<Booking> response = client.exchange(HttpRequest.POST("/bookings", new BookingRequest("Grand"))
            .header("Link", TRANSACTION_LINK), Booking) // <1>
        Booking booking = response.body()

        then:
        response.status == HttpStatus.CREATED
        booking.status == BookingStatus.PROVISIONAL
        response.header("Link") == "<http://localhost:8081/bookings/${booking.id}>; rel=\"https://otmm.oracle.com/tcc-participant\"; timeLimit=60000".toString() // <2>

        expect:
        client.retrieve(HttpRequest.PUT("/bookings/${booking.id}", ""), Booking).status == BookingStatus.CONFIRMED // <3>
    }

    void "the coordinator cancels a reservation"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        Booking booking = client.retrieve(HttpRequest.POST("/bookings", new BookingRequest("Grand"))
            .header("Link", TRANSACTION_LINK), Booking)

        expect:
        client.retrieve(HttpRequest.DELETE("/bookings/${booking.id}"), Booking).status == BookingStatus.CANCELLED
    }
}
