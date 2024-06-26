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

import io.micronaut.http.client.annotation.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import io.micronaut.core.annotation.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Client("/") // <1>
public interface GreetingClient {

    @GetMapping("/greeting{?name}") // <2>
    Greeting greet(@Nullable String name);

    @PostMapping("/greeting") // <3>
    Greeting greetByPost(@RequestBody Greeting greeting); // <4>

    @DeleteMapping("/greeting") // <5>
    HttpStatus deletePost();

    @GetMapping("/nested/greeting{?name}") // <2>
    Greeting nestedGreet(@RequestParam @Nullable String name); // <6>

    @GetMapping("/greeting-status{?name}") // <2>
    ResponseEntity<Greeting> greetWithStatus(@RequestParam @Nullable String name); // <6>
}
