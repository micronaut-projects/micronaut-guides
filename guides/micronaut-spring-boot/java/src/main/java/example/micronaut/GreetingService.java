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

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Pattern;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service // <1>
@Validated // <2>
public class GreetingService {

    private final AtomicLong counter = new AtomicLong();

    private final GreetingConfiguration greetingConfiguration;

    private final AtomicReference<Greeting> lastGreeting = new AtomicReference<>();

    public GreetingService(GreetingConfiguration greetingConfiguration) { // <3>
        this.greetingConfiguration = greetingConfiguration;
    }

    public Greeting greeting(@Pattern(regexp = "\\D+") String name) { // <4>
        final Greeting greeting = new Greeting(counter.incrementAndGet(),
                String.format(greetingConfiguration.getTemplate(), name));
        lastGreeting.set(greeting);
        return greeting;
    }

    public Optional<Greeting> getLastGreeting() {
        return Optional.ofNullable(lastGreeting.get());
    }
}
