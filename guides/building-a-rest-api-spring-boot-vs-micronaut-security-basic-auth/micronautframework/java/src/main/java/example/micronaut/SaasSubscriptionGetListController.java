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

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;

import java.security.Principal;

@Controller("/subscriptions") // <1>
@Secured("SAAS_SUBSCRIPTION_OWNER") // <2>
class SaasSubscriptionGetListController {

    private static final Sort CENTS = Sort.of(Sort.Order.asc("cents"));
    private final SaasSubscriptionRepository repository;

    SaasSubscriptionGetListController(SaasSubscriptionRepository repository) { // <3>
        this.repository = repository;
    }

    @Get // <4>
    Iterable<SaasSubscription> findAll(Pageable pageable, Principal principal) { // <5>
        Page<SaasSubscription> page = repository.findByOwner(principal.getName(), pageable.getSort().isSorted()
                ? pageable
                : Pageable.from(pageable.getNumber(), pageable.getSize(), CENTS)
        );  // <6>
        return page.getContent();
    }
}
