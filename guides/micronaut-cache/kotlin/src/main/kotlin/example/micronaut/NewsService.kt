/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.cache.annotation.CacheConfig
import io.micronaut.cache.annotation.CacheInvalidate
import io.micronaut.cache.annotation.CachePut
import io.micronaut.cache.annotation.Cacheable
import java.time.Month
import java.util.concurrent.TimeUnit
import jakarta.inject.Singleton

@Singleton // <1>
@CacheConfig("headlines") // <2>
open class NewsService {
    val headlines = mutableMapOf(
            Month.NOVEMBER to listOf("Micronaut Graduates to Trial Level in Thoughtworks technology radar Vol.1",
            "Micronaut AOP: Awesome flexibility without the complexity"),
            Month.OCTOBER to listOf("Micronaut AOP: Awesome flexibility without the complexity"))

    @Cacheable // <3>
    open fun headlines(month: Month): List<String>? {
        return try {
            TimeUnit.SECONDS.sleep(3) // <4>
            headlines[month]
        } catch (e: InterruptedException) {
            null
        }
    }

    @CachePut(parameters = ["month"]) // <5>
    open fun addHeadline(month: Month, headline: String): List<String>? {
        val l = headlines.getOrDefault(month, emptyList()).toMutableList()
        l.add(headline)
        headlines[month] = l
        return headlines[month]
    }

    @CacheInvalidate(parameters = ["month"]) // <6>
    open fun removeHeadline(month: Month, headline: String?) {
        val l = headlines.getOrDefault(month, emptyList()).toMutableList()
        l.remove(headline)
        headlines[month] = l
    }
}
