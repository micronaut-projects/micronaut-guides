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
package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import io.micronaut.transaction.exceptions.OracleTransactionPriorityException
import jakarta.inject.Singleton

@Produces
@Singleton
class TransactionPriorityExceptionHandler(
    private val errorResponseProcessor: ErrorResponseProcessor<*>
) : ExceptionHandler<OracleTransactionPriorityException, HttpResponse<*>> { // <1>

    override fun handle(request: HttpRequest<*>, exception: OracleTransactionPriorityException): HttpResponse<*> {
        val errorContext = ErrorContext.builder(request)
            .cause(exception)
            .errorMessage("Oracle rolled back this operation in favor of a higher-priority transaction") // <2>
            .build()
        return errorResponseProcessor.processResponse(errorContext, HttpResponse.status<Any>(HttpStatus.CONFLICT)) // <3>
    }
}
