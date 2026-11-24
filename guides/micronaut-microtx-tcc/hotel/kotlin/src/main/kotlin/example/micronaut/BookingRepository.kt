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
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Singleton
class BookingRepository {

    private val bookings = ConcurrentHashMap<String, Booking>()

    fun reserve(hotel: String): Booking {
        val booking = Booking(UUID.randomUUID().toString(), hotel, BookingStatus.PROVISIONAL)
        bookings[booking.id] = booking
        return booking
    }

    fun find(id: String): Booking? = bookings[id]

    fun update(id: String, status: BookingStatus): Booking? =
        bookings.computeIfPresent(id) { _, booking -> booking.copy(status = status) }
}
