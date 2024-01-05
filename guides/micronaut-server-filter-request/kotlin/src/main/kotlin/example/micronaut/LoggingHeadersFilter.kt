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

@ServerFilter(Filter.MATCH_ALL_PATTERN)
open class LoggingHeadersFilter : Ordered {

    @RequestFilter
    fun filterRequest(request: HttpRequest<*>) {
        if (LOG.isTraceEnabled) {
            logHeaders(request)
        }
    }

    fun logHeaders(headers: HttpHeaders) {
        for (headerName in headers.names()) {
            if (headerName.equals(HttpHeaders.AUTHORIZATION, ignoreCase = true)) {
                continue
            }
            log(headerName, headers[headerName])
        }
    }

    protected open fun log(headerName: String, headerValue: String?) {
        LOG.trace("H {}:{}", headerName, headerValue)
    }

    private fun logHeaders(request: HttpRequest<*>) {
        LOG.trace("{} {} uri {}", request.method, request.path, request.uri.toString())
        logHeaders(request.headers)
    }

    override fun getOrder() = ServerFilterPhase.FIRST.order()

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(LoggingHeadersFilter::class.java)
    }
}