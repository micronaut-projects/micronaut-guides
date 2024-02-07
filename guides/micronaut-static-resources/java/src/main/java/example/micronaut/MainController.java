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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

import static io.micronaut.http.MediaType.TEXT_HTML;

@Controller // <1>
class MainController {

    private final MessageService messageService;

    public MainController(MessageService messageService) { // <2>
        this.messageService = messageService;
    }

    @View("index.html") // <3>
    @Get(value = "/hello/{name}", produces = TEXT_HTML) // <4>
    Map<String, String> index(@NonNull @NotBlank String name) { // <5>
        return Map.of("message", messageService.sayHello(name));
    }
}