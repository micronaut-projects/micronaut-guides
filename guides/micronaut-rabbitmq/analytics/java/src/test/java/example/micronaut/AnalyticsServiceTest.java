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
package example.micronaut;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.List;

@MicronautTest // <1>
public class AnalyticsServiceTest {

    @Inject // <2>
    AnalyticsService analyticsService;

    @Test
    void testUpdateBookAnalyticsAndGetAnalytics() {
        Book b1 = new Book("1491950358", "Building Microservices");
        Book b2 = new Book("1680502395", "Release It!");

        analyticsService.updateBookAnalytics(b1);
        analyticsService.updateBookAnalytics(b1);
        analyticsService.updateBookAnalytics(b1);
        analyticsService.updateBookAnalytics(b2);

        List<BookAnalytics> analytics = analyticsService.listAnalytics();
        assertEquals(2, analytics.size());

        assertEquals(3, findBookAnalytics(b1, analytics).getCount());
        assertEquals(1, findBookAnalytics(b2, analytics).getCount());
    }

    private BookAnalytics findBookAnalytics(Book b, List<BookAnalytics> analytics) {
        return analytics
                .stream()
                .filter(bookAnalytics -> bookAnalytics.getBookIsbn().equals(b.getIsbn()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }
}
