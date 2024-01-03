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
package example.micronaut;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.simple.SimpleHttpHeaders;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LoggingHeadersFilterTest extends LoggingHeadersFilter {
    @Test
    void sensistiveHeadersAreNotLogged() {
        Map<String, String> headersMap = Map.of("host", "micronaut-foo-bar-yyyy-uc.a.run.app",
                "accept", "application/json",
                "authorization", "Bearer yyy",
                "content-type", "application/json",
                "content-length", "523",
                "x-forwarded-for", "107.178.207.38",
                "x-forwarded-proto", "https",
                "forwarded:for", "\"107.178.207.38\";proto=https");
        HttpHeaders headers = new SimpleHttpHeaders(headersMap, null);
        LoggingHeadersFilterOverride filter = new LoggingHeadersFilterOverride();
        filter.logHeaders(headers);
        assertFalse(filter.getHeaders().containsKey(HttpHeaders.AUTHORIZATION));
        assertFalse(filter.getHeaders().containsKey("authorization"));
        assertTrue(filter.getHeaders().containsKey("host"));
        filter.clear();

    }
}