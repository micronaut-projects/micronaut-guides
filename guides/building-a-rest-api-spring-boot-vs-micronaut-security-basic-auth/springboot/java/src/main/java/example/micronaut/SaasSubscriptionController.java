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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController // <1>
@RequestMapping("/subscriptions") // <2>
class SaasSubscriptionController {

    private final SaasSubscriptionRepository repository;

    private SaasSubscriptionController(SaasSubscriptionRepository repository) { // <3>
        this.repository = repository;
    }

    @GetMapping("/{id}") // <4>
    private ResponseEntity<SaasSubscription> findById(@PathVariable Long id,  // <5>
                                                      Principal principal) { // <6>
        return Optional.ofNullable(repository.findByIdAndOwner(id, principal.getName()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
