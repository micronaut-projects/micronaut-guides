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

import jakarta.inject.Singleton

import java.util.concurrent.ConcurrentHashMap

@Singleton
class BookingRepository {

    private final Map<String, Booking> bookings = new ConcurrentHashMap<>()

    Booking reserve(String hotel) {
        Booking booking = new Booking(UUID.randomUUID().toString(), hotel, BookingStatus.PROVISIONAL)
        bookings.put(booking.id, booking)
        booking
    }

    Optional<Booking> find(String id) {
        Optional.ofNullable(bookings.get(id))
    }

    Optional<Booking> update(String id, BookingStatus status) {
        Optional.ofNullable(bookings.computeIfPresent(id, (key, booking) -> booking.withStatus(status)))
    }
}
