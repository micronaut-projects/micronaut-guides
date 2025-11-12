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
/*
//tag::package[]
package example.micronaut
//end::package[]
*/
//tag::imports[]

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Recoverable
import org.reactivestreams.Publisher
//end::imports[]

/*
//tag::hardcoded[]
@Client("http://localhost:8081") // <1>
@Recoverable(api = BookCatalogueOperations::class)
//end::hardcoded[]
*/
//tag::eureka[]
@Client(id = "bookcatalogue") // <1>
@Recoverable(api = BookCatalogueOperations::class)
//end::eureka[]
//tag::clazz[]
interface BookCatalogueClient : BookCatalogueOperations {

    @Get("/books")
    override fun findAll(): Publisher<Book>
}
//end::clazz[]
