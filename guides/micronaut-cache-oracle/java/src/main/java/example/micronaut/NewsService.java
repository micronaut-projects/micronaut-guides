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

import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;

import java.time.Month;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton // <1>
@CacheConfig("headlines") // <2>
public class NewsService {

    private final AtomicInteger invocations = new AtomicInteger();

    @Cacheable // <3>
    public News headline(Month month) {
        invocations.incrementAndGet();
        return new News("Micronaut cache news for " + month);
    }

    int invocations() {
        return invocations.get();
    }
}
