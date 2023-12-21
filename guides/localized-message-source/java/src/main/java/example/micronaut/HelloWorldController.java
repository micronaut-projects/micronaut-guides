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

import io.micronaut.context.LocalizedMessageSource;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import java.util.Optional;

@Controller // <1>
public class HelloWorldController {

    private final LocalizedMessageSource messageSource;

    public HelloWorldController(LocalizedMessageSource messageSource) { // <2>
        this.messageSource = messageSource;
    }

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get // <4>
    Optional<String> index() { // <5>
        return messageSource.getMessage("hello.world"); // <6>
    }
}
