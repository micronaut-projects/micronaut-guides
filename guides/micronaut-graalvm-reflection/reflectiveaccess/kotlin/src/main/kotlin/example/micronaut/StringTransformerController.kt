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

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.annotation.QueryValue

@Controller("/transformer") // <1>
class StringTransformerController(private val transformer: StringTransformer) { // <2>

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get("/capitalize{?q}") // <4>
    fun capitalize(@Nullable @QueryValue q: String?): String? = // <5>
        transformer.transform(q, "example.micronaut.StringCapitalizer", "capitalize")

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get("/reverse{?q}") // <4>
    fun reverse(@Nullable @QueryValue q: String?): String? = // <5>
        transformer.transform(q, "example.micronaut.StringReverser", "reverse")
}
