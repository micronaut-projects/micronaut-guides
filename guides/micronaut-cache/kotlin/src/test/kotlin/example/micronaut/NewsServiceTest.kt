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
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.Timeout
import java.time.Month
import jakarta.inject.Inject

@TestMethodOrder(OrderAnnotation::class) // <1>
@MicronautTest(startApplication = false)  // <2>
internal class NewsServiceTest {
    @Inject
    lateinit var newsService : NewsService // <3>

    @Timeout(4) // <4>
    @Test
    @Order(1) // <5>
    fun firstInvocationOfNovemberDoesNotHitCache() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        assertEquals(2, headlines!!.size)
    }

    @Timeout(1) // <4>
    @Test
    @Order(2) // <5>
    fun secondInvocationOfNovemberHitsCache() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        assertEquals(2, headlines!!.size)
    }

    @Timeout(4) // <4>
    @Test
    @Order(3) // <5>
    fun firstInvocationOfOctoberDoesNotHitCache() {
        val headlines = newsService.headlines(Month.OCTOBER)
        assertEquals(1, headlines!!.size)
    }

    @Timeout(1) // <4>
    @Test
    @Order(4) // <5>
    fun secondInvocationOfOctoberHitsCache() {
        val headlines = newsService.headlines(Month.OCTOBER)
        assertEquals(1, headlines!!.size)
    }

    @Timeout(1) // <4>
    @Test
    @Order(5) // <5>
    fun addingAHeadlineToNovemberUpdatesCache() {
        val headlines = newsService.addHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released")
        assertEquals(3, headlines!!.size)
    }

    @Timeout(1) // <4>
    @Test
    @Order(6) // <5>
    fun novemberCacheWasUpdatedByCachePutAndThusTheValueIsRetrievedFromTheCache() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        assertEquals(3, headlines!!.size)
    }

    @Timeout(1) // <4>
    @Test
    @Order(7) // <5>
    fun invalidateNovemberCacheWithCacheInvalidate() {
        assertDoesNotThrow { newsService.removeHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released") }
    }

    @Timeout(1) // <4>
    @Test
    @Order(8) // <5>
    fun octoberCacheIsStillValid() {
        val headlines = newsService.headlines(Month.OCTOBER)
        assertEquals(1, headlines!!.size)
    }

    @Timeout(4) // <4>
    @Test
    @Order(9) // <5>
    fun novemberCacheWasInvalidated() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        assertEquals(2, headlines!!.size)
    }
}
