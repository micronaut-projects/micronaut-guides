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
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;

import java.net.URI;
import java.security.Principal;

@Controller("/subscriptions") // <1>
@Secured("SAAS_SUBSCRIPTION_OWNER") // <2>
class SaasSubscriptionPostController {

    private final SaasSubscriptionRepository repository;

    SaasSubscriptionPostController(SaasSubscriptionRepository repository) { // <3>
        this.repository = repository;
    }

    @Post // <4>
    HttpResponse<?> createSaasSubscription(@Body SaasSubscriptionSave body, Principal principal) { // <5>
        SaasSubscription subscription = new SaasSubscription(null, body.name(), body.cents(), principal.getName());
        SaasSubscription savedSaasSubscription = repository.save(subscription);
        URI locationOfNewSaasSubscription = UriBuilder.of("/subscriptions") // <6>
                .path(savedSaasSubscription.id().toString())
                .build();
        return HttpResponse.created(locationOfNewSaasSubscription);
    }
}
