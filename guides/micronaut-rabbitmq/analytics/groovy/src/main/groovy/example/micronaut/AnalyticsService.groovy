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

import jakarta.inject.Singleton
import java.util.concurrent.ConcurrentHashMap

@CompileStatic
@Singleton
class AnalyticsService {

    private final Map<Book, Long> bookAnalytics = new ConcurrentHashMap<>() // <1>

    void updateBookAnalytics(Book book) { // <2>
        bookAnalytics.compute(book, (k, v) -> {
            v == null ? 1L : v + 1
        })
    }

    List<BookAnalytics> listAnalytics() { // <3>
        bookAnalytics.collect { e -> new BookAnalytics(e.key.isbn, e.value) }
    }
}
