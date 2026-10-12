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
package example.micronaut.dto;

import example.micronaut.entity.Booking;
import example.micronaut.entity.BookingStatus;

import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;

@Serdeable
public record BookingView(Long id, String seatId, BigDecimal amount, BookingStatus status, String paymentReference) {
    public static BookingView from(Booking booking) {
        return new BookingView(booking.getId(), booking.getSeatId(), booking.getAmount(),
            booking.getStatus(), booking.getPaymentReference());
    }
}
