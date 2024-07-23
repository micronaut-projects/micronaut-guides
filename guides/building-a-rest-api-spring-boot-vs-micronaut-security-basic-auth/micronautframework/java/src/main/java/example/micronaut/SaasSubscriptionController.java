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

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import java.security.Principal;
import io.micronaut.security.annotation.Secured;

@Controller("/subscriptions") // <1>
@Secured("SAAS_SUBSCRIPTION_OWNER") // <2>
class SaasSubscriptionController {

    private final SaasSubscriptionRepository repository;

    SaasSubscriptionController(SaasSubscriptionRepository repository) { // <3>
        this.repository = repository;
    }

    @Get("/{id}") // <4>
    HttpResponse<SaasSubscription> findById(@PathVariable Long id, // <5>
                                            Principal principal) { // <6>
        return repository.findByIdAndOwner(id, principal.getName())
                .map(HttpResponse::ok)
                .orElseGet(HttpResponse::notFound); // <7>
    }
}
