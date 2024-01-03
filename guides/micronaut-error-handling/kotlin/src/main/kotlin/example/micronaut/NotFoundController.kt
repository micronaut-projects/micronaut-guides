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

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.hateoas.Link
import io.micronaut.views.ViewsRenderer

@Controller("/notfound") // <1>
class NotFoundController(private val viewsRenderer: ViewsRenderer<Any, Any>) { // <2>

    @Error(status = HttpStatus.NOT_FOUND, global = true) // <3>
    fun notFound(request: HttpRequest<Any>): HttpResponse<*> {
        if (request.headers.accept().any { it.name.contains(MediaType.TEXT_HTML) }) { // <4>
            return HttpResponse
                .ok(viewsRenderer.render("notFound", emptyMap<Any, Any>(), request))
                .contentType(MediaType.TEXT_HTML)
        }
        val error = JsonError("Page Not Found").link(Link.SELF, Link.of(request.uri))
        return HttpResponse.notFound(error) // <5>
    }
}