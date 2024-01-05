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

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest
class AnalyticsServiceSpec extends Specification {

    @Inject
    AnalyticsService analyticsService

    void 'test update book analytics and get analytics'() {
        given:
        Book b1 = new Book('1491950358', 'Building Microservices')
        Book b2 = new Book('1680502395', 'Release It!')

        when:
        analyticsService.updateBookAnalytics b1
        analyticsService.updateBookAnalytics b1
        analyticsService.updateBookAnalytics b1
        analyticsService.updateBookAnalytics b2

        List<BookAnalytics> analytics = analyticsService.listAnalytics()

        then:
        2 == analytics.size()
        3 == findBookAnalytics(b1, analytics).count
        1 == findBookAnalytics(b2, analytics).count
    }

    private BookAnalytics findBookAnalytics(Book b, List<BookAnalytics> analytics) {
        BookAnalytics bookAnalytics = analytics.find { it.bookIsbn == b.isbn }
        if (!bookAnalytics) {
            throw new RuntimeException('Book not found')
        }
        bookAnalytics
    }
}
