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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.constraints.Pattern;

@RestController // <1>
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) { // <2>
        this.greetingService = greetingService;
    }

    @GetMapping("/greeting") // <3>
    public Greeting greeting(
            @RequestParam(value="name", defaultValue="World") @Pattern(regexp = "\\D+") String name) {  // <4>
        return greetingService.greeting(name);
    }

    @PostMapping("/greeting") // <5>
    public Greeting greetingByPost(@RequestBody Greeting greeting) { // <6>
        return greetingService.greeting(greeting.getContent());
    }

    @DeleteMapping("/greeting") // <7>
    public ResponseEntity<?> deleteGreeting() { // <8>
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Foo", "Bar");
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT); // <8>
    }

    @GetMapping("/greeting-status") // <3>
    @ResponseStatus(code = HttpStatus.CREATED) // <9>
    public Greeting greetingWithStatus(
            @RequestParam(value="name", defaultValue="World") @Pattern(regexp = "\\D+") String name) {
        return greetingService.greeting(name);
    }
}