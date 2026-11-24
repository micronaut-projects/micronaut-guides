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

import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Sort
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/subscriptions") // <1>
class SaasSubscriptionGetListController(private val repository: SaasSubscriptionRepository) { // <2>

    @Get // <3>
    fun findAll(pageable: Pageable): Iterable<SaasSubscription> { // <4>
        val page = repository.findAll(
            if (pageable.sort.isSorted) {
                pageable
            } else {
                Pageable.from(pageable.number, pageable.size, CENTS)
            }
        ) // <5>
        return page.content
    }

    private companion object {
        val CENTS: Sort = Sort.of(Sort.Order.asc("cents"))
    }
}
