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

import io.micronaut.http.annotation.Filter
import io.micronaut.http.annotation.ServerFilter
import java.util.concurrent.ConcurrentHashMap

@ServerFilter(Filter.MATCH_ALL_PATTERN)
class LoggingHeadersFilterOverride : LoggingHeadersFilter() {

    val headers = ConcurrentHashMap<String, String?>()

    override fun log(headerName: String, headerValue: String?) {
        headers[headerName] = headerValue
    }

    fun clear() {
        headers.clear()
    }

    fun getHeaders(): Map<String, String?> {
        return headers
    }
}
