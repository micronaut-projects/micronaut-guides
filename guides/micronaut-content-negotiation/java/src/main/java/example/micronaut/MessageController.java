/*
 * Copyright 2017-2023 original authors
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
package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.ModelAndView;

import java.util.Collections;
import java.util.Map;

@Controller // <1>
class MessageController {

    @Produces(value = {MediaType.TEXT_HTML, MediaType.APPLICATION_JSON}) // <2>
    @Get // <3>
    HttpResponse<?> index(HttpRequest<?> request) { // <4>
        Map<String, Object> model = Collections.singletonMap("message", "Hello World");
        Object body = accepts(request, MediaType.TEXT_HTML_TYPE)
                ? new ModelAndView<>("message.html", model)
                : model;
        return HttpResponse.ok(body);
    }

    private static boolean accepts(HttpRequest<?> request, MediaType mediaType) {
        return request.getHeaders()
                .accept()
                .stream()
                .anyMatch(it -> it.getName().contains(mediaType));
    }
}