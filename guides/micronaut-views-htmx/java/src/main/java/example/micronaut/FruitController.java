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
package example.micronaut;

import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.htmx.http.HtmxRequestHeaders;
import io.micronaut.views.htmx.http.HtmxResponse;
import io.micronaut.views.htmx.http.HtmxResponseHeaders;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.Map;

@Controller("/fruits") // <1>
class FruitController {

    @Produces(MediaType.TEXT_HTML) // <2>
    @Get // <3>
    ModelAndView<Map<String, Object>> index(@Nullable HtmxRequestHeaders htmxRequestHeaders) { // <4>
        Map<String, Object> model = fruit("Apple", "Red");
        return htmxRequestHeaders == null
                ? new ModelAndView<>("fruits.html", model)
                : new ModelAndView<>("fruit.html", model);
    }

    @Produces(MediaType.TEXT_HTML)
    @Post // <5>
    Object select(@Nullable HtmxRequestHeaders htmxRequestHeaders) { // <6>
        if (htmxRequestHeaders == null) {
            return new ModelAndView<>("fruits.html", fruit("Banana", "Yellow"));
        }
        return HtmxResponse.builder()
                .modelAndView(new ModelAndView<>("fruit.html", fruit("Banana", "Yellow")))
                .modelAndView(new ModelAndView<>("message.html", Map.of("message", "Selected Banana")))
                .build();
    }

    @Produces(MediaType.TEXT_HTML)
    @Get("/refresh")
    HttpResponse<?> refresh(@Nullable HtmxRequestHeaders htmxRequestHeaders) { // <7>
        if (htmxRequestHeaders == null) {
            return HttpResponse.seeOther(URI.create("/fruits"));
        }
        return HttpResponse.ok().header(HtmxResponseHeaders.HX_REFRESH, StringUtils.TRUE);
    }

    private static Map<String, Object> fruit(String name, String color) {
        return Map.of("fruitName", name, "fruitColor", color);
    }
}
