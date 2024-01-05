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

import io.micronaut.http.HttpHeaders
import io.micronaut.http.simple.SimpleHttpHeaders
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class LoggingHeadersFilterTest : LoggingHeadersFilter() {

    @Test
    fun sensistiveHeadersAreNotLogged() {
        val headersMap = mapOf(
            "host" to "micronaut-foo-bar-yyyy-uc.a.run.app",
            "accept" to "application/json",
            "authorization" to "Bearer yyy",
            "content-type" to "application/json",
            "content-length" to "523",
            "x-forwarded-for" to "107.178.207.38",
            "x-forwarded-proto" to "https",
            "forwarded:for" to "\"107.178.207.38\";proto=https"
        )
        val headers: HttpHeaders = SimpleHttpHeaders(headersMap, null)
        val filter = LoggingHeadersFilterOverride()
        filter.logHeaders(headers)
        assertFalse(filter.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
        assertFalse(filter.getHeaders().containsKey("authorization"))
        assertTrue(filter.getHeaders().containsKey("host"))
        filter.clear()
    }
}