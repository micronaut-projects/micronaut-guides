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
package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
class BookingControllerTest {

    private static final String TRANSACTION_LINK = "<http://localhost:9000/api/v1/tcc-transaction/a1b2c3>; rel=\"https://otmm.oracle.com/tcc-transaction\"";

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void aReservationJoinsTheTransactionOfTheCaller() {
        BlockingHttpClient client = httpClient.toBlocking();

        HttpResponse<Booking> response = client.exchange(HttpRequest.POST("/bookings", new BookingRequest("Grand"))
            .header("Link", TRANSACTION_LINK), Booking.class); // <1>

        assertEquals(HttpStatus.CREATED, response.getStatus());
        Booking booking = response.body();
        assertNotNull(booking);
        assertEquals(BookingStatus.PROVISIONAL, booking.status());
        assertEquals("<http://localhost:8081/bookings/" + booking.id() + ">; rel=\"https://otmm.oracle.com/tcc-participant\"; timeLimit=60000",
            response.header("Link")); // <2>

        assertEquals(BookingStatus.CONFIRMED, client.retrieve(HttpRequest.PUT("/bookings/" + booking.id(), ""), Booking.class).status()); // <3>
    }

    @Test
    void theCoordinatorCancelsAReservation() {
        BlockingHttpClient client = httpClient.toBlocking();
        Booking booking = client.retrieve(HttpRequest.POST("/bookings", new BookingRequest("Grand"))
            .header("Link", TRANSACTION_LINK), Booking.class);

        assertEquals(BookingStatus.CANCELLED, client.retrieve(HttpRequest.DELETE("/bookings/" + booking.id()), Booking.class).status());
    }
}
