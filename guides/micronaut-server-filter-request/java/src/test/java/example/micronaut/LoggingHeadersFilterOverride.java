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

import io.micronaut.http.annotation.ServerFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.micronaut.http.annotation.Filter.MATCH_ALL_PATTERN;

@ServerFilter(MATCH_ALL_PATTERN)
public class LoggingHeadersFilterOverride extends LoggingHeadersFilter {

    private final Map<String, String> headers = new ConcurrentHashMap<>();

    @Override
    protected void log(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

    public void clear() {
        headers.clear();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
