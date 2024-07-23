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

import java.net.URI;

@Controller("/subscriptions") // <1>
class SaasSubscriptionPostController {

    private final SaasSubscriptionRepository repository;

    SaasSubscriptionPostController(SaasSubscriptionRepository repository) { // <2>
        this.repository = repository;
    }

    @Post // <3>
    HttpResponse<?> createSaasSubscription(@Body SaasSubscription newSaasSubscription) { // <4>
        SaasSubscription savedSaasSubscription = repository.save(newSaasSubscription);
        URI locationOfNewSaasSubscription = UriBuilder.of("/subscriptions") // <5>
                .path(savedSaasSubscription.id().toString())
                .build();
        return HttpResponse.created(locationOfNewSaasSubscription);
    }
}
