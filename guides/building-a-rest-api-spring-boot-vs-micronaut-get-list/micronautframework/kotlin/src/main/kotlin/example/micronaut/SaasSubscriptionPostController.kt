/*
 * Copyright 2017-2026 original authors
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
package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder

@Controller("/subscriptions") // <1>
class SaasSubscriptionPostController(private val repository: SaasSubscriptionRepository) { // <2>

    @Post // <3>
    fun createSaasSubscription(@Body newSaasSubscription: SaasSubscription): HttpResponse<*> { // <4>
        val savedSaasSubscription = repository.save(newSaasSubscription)
        val locationOfNewSaasSubscription = UriBuilder.of("/subscriptions") // <5>
            .path(savedSaasSubscription.id.toString())
            .build()
        return HttpResponse.created<Any>(locationOfNewSaasSubscription)
    }
}
