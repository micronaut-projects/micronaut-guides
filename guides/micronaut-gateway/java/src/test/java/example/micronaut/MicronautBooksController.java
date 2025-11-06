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

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.List;

@Requires(property = "framework", value = "micronaut")
@Controller("/micronaut/books")
class MicronautBooksController {

    @Get
    List<Book> books() {
        return List.of(
                new Book("Introducing Micronaut: Build, Test, and Deploy Java Microservices on Oracle Cloud"),
                new Book("Building Microservices with Micronaut®")
        );
    }
}
