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
package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import io.micronaut.views.ViewsRenderer;

import java.util.Collections;

@Controller("/notfound") // <1>
public class NotFoundController {

    private final ViewsRenderer viewsRenderer;

    public NotFoundController(ViewsRenderer viewsRenderer) { // <2>
        this.viewsRenderer = viewsRenderer;
    }

    @Error(status = HttpStatus.NOT_FOUND, global = true)  // <3>
    public HttpResponse notFound(HttpRequest request) {
        if (request.getHeaders()
                .accept()
                .stream()
                .anyMatch(mediaType -> mediaType.getName().contains(MediaType.TEXT_HTML))) { // <4>
            return HttpResponse.ok(viewsRenderer.render("notFound", Collections.emptyMap(), request))
                    .contentType(MediaType.TEXT_HTML);
        }

        JsonError error = new JsonError("Page Not Found")
                .link(Link.SELF, Link.of(request.getUri()));

        return HttpResponse.<JsonError>notFound()
                .body(error); // <5>
    }
}
