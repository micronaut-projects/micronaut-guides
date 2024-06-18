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

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController // <1>
@RequestMapping("/subscriptions") // <2>
public class SaasSubscriptionPostController {

    private final SaasSubscriptionRepository repository;

    private SaasSubscriptionPostController(SaasSubscriptionRepository repository) { // <3>
        this.repository = repository;
    }

    @PostMapping // <4>
    private ResponseEntity<Void> createSaasSubscription(@RequestBody SaasSubscriptionSave body, // <5>
                                                        UriComponentsBuilder ucb,
                                                        Principal principal) {
        SaasSubscription subscription = new SaasSubscription(null, body.name(), body.cents(), principal.getName());
        SaasSubscription savedSubscription = repository.save(subscription);
        URI locationOfSubscription = ucb
                .path("subscriptions/{id}")
                .buildAndExpand(savedSubscription.id())
                .toUri();
        return ResponseEntity.created(locationOfSubscription).build();
    }
}
