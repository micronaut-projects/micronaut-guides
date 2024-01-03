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

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Component // <1>
public class GreetingJob {
    private static final Logger LOG = LoggerFactory.getLogger(GreetingJob.class);

    private final GreetingService greetingService;

    public GreetingJob(GreetingService greetingService) { // <2>
        this.greetingService = greetingService;
    }

    @Scheduled(fixedDelayString = "30s") // <3>
    void printLastGreeting() {
        final Optional<Greeting> lastGreeting = greetingService.getLastGreeting();
        lastGreeting.ifPresent(greeting -> {
            LOG.info("Last Greeting was = {}", greeting.getContent());
        });
    }
}
