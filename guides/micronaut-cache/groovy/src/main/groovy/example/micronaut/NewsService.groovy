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

import groovy.transform.CompileStatic
import io.micronaut.cache.annotation.CacheConfig
import io.micronaut.cache.annotation.CacheInvalidate
import io.micronaut.cache.annotation.CachePut
import io.micronaut.cache.annotation.Cacheable
import jakarta.inject.Singleton
import java.time.Month
import java.util.concurrent.TimeUnit

@CompileStatic
@Singleton // <1>
@CacheConfig("headlines") // <2>
class NewsService {

    Map<Month, List<String>> headlines = [
        (Month.NOVEMBER): ["Micronaut Graduates to Trial Level in Thoughtworks technology radar Vol.1",
                "Micronaut AOP: Awesome flexibility without the complexity"],
        (Month.OCTOBER): ["Micronaut AOP: Awesome flexibility without the complexity"]
    ]

    @Cacheable // <3>
    List<String> headlines(Month month) {
        TimeUnit.SECONDS.sleep(3) // <4>
        headlines[month]
    }

    @CachePut(parameters = ["month"]) // <5>
    List<String> addHeadline(Month month, String headline) {
        if (headlines.containsKey(month)) {
            List<String> lines = headlines[month]
            lines << headline
            headlines.put(month, lines)
        } else {
            headlines[month] = [headline]
        }
        headlines[month]
    }

    @CacheInvalidate(parameters = ["month"]) // <6>
    void removeHeadline(Month month, String headline) {
        if (headlines.containsKey(month)) {
            List<String> lines = headlines[month]
            lines -= headline
            headlines.put(month, lines)
        }
    }
}
