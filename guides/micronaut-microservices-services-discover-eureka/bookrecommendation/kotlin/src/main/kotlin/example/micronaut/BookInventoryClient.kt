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
//tag::packageandimports[]
package example.micronaut

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Recoverable
import reactor.core.publisher.Mono
import jakarta.validation.constraints.NotBlank
//end::packageandimports[]

/*
//tag::harcoded[]
@Client("http://localhost:8082") // <1>
@Recoverable(api = BookInventoryOperations::class)
//end::harcoded[]
*/
//tag::eureka[]
@Client(id = "bookinventory") // <1>
@Recoverable(api = BookInventoryOperations::class)
//end::eureka[]
//tag::clazz[]
interface BookInventoryClient : BookInventoryOperations {

    @Consumes(TEXT_PLAIN)
    @Get("/books/stock/{isbn}")
    override fun stock(@NotBlank isbn: String): Mono<Boolean>
}
//end::clazz[]
