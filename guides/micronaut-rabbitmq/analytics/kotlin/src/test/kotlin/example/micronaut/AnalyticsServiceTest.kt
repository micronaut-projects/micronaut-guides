/*
 * Copyright 2017-2024 original authors
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

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@MicronautTest // <1>
class AnalyticsServiceTest {

    @Inject // <2>
    lateinit var analyticsService: AnalyticsService

    @Test
    fun testUpdateBookAnalyticsAndGetAnalytics() {
        val b1 = Book("1491950358", "Building Microservices")
        val b2 = Book("1680502395", "Release It!")

        analyticsService.updateBookAnalytics(b1)
        analyticsService.updateBookAnalytics(b1)
        analyticsService.updateBookAnalytics(b1)
        analyticsService.updateBookAnalytics(b2)

        val analytics = analyticsService.listAnalytics()

        assertEquals(2, analytics.size)
        assertEquals(3, findBookAnalytics(b1, analytics).count)
        assertEquals(1, findBookAnalytics(b2, analytics).count)
    }

    private fun findBookAnalytics(b: Book, analytics: List<BookAnalytics>): BookAnalytics {
        val ba : BookAnalytics? = analytics.filter { (bookIsbn) -> bookIsbn == b.isbn }.firstOrNull()
        return ba ?: throw RuntimeException("Book not found")
    }
}
