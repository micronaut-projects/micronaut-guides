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
package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false) // <1>
class NewsServiceTest {

    @Inject // <2>
    NewsService newsService;

    @Test
    void cachesHeadlinesInOracle() {
        assertEquals(0, newsService.invocations());
        assertEquals("Micronaut cache news for NOVEMBER", newsService.headline(Month.NOVEMBER).headline());
        assertEquals("Micronaut cache news for NOVEMBER", newsService.headline(Month.NOVEMBER).headline());
        assertEquals(1, newsService.invocations()); // <3>
    }
}
