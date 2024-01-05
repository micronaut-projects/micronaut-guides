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

import io.micronaut.core.order.Ordered
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Filter
import io.micronaut.http.annotation.RequestFilter
import io.micronaut.http.annotation.ServerFilter
import io.micronaut.http.filter.ServerFilterPhase
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ServerFilter(Filter.MATCH_ALL_PATTERN) // <1>
class LoggingHeadersFilter : Ordered {

    @RequestFilter // <2>
    fun filterRequest(request: HttpRequest<*>) {
        if (LOG.isTraceEnabled) {
            val headers = request.headers
            for (headerName in headers.names()) {
                if (headerName.equals(HttpHeaders.AUTHORIZATION, ignoreCase = true)) {
                    continue
                }
                LOG.trace("{} {} H {}:{}", request.method, request.path, headerName, headers[headerName])
            }
        }
    }

    override fun getOrder() = ServerFilterPhase.FIRST.order() // <3>

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(LoggingHeadersFilter::class.java)
    }
}