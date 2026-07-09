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

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController // <1>
@RequestMapping("/subscriptions") // <2>
class SaasSubscriptionPostController(private val repository: SaasSubscriptionRepository) { // <3>

    @PostMapping // <4>
    fun createSaasSubscription(
        @RequestBody newSaasSubscription: SaasSubscription, // <5>
        ucb: UriComponentsBuilder
    ): ResponseEntity<Void> {
        val subscription = repository.save(newSaasSubscription)
        val locationOfSubscription = ucb
            .path("subscriptions/{id}")
            .buildAndExpand(subscription.id)
            .toUri()
        return ResponseEntity.created(locationOfSubscription).build()
    }
}
