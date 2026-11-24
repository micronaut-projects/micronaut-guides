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
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.views.ModelAndView

@Controller // <1>
class MessageController {

    @Produces(value = [MediaType.TEXT_HTML, MediaType.APPLICATION_JSON]) // <2>
    @Get // <3>
    fun index(request: HttpRequest<*>): HttpResponse<*> { // <4>
        val model = mapOf("message" to "Hello World")
        if (prefersHtml(request)) {
            return HttpResponse.ok(ModelAndView("message.html", model))
        }
        return HttpResponse.ok(model).contentType(MediaType.APPLICATION_JSON_TYPE)
    }

    private fun prefersHtml(request: HttpRequest<*>): Boolean {
        for (accepted in request.headers.accept()) { // ordered by quality
            if (accepted == MediaType.TEXT_HTML_TYPE) {
                return true
            }
            if (accepted == MediaType.APPLICATION_JSON_TYPE) {
                return false
            }
        }
        return false
    }
}
