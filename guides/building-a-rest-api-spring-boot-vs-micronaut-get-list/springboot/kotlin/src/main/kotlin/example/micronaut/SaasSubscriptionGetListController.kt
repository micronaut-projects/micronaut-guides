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

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController // <1>
@RequestMapping("/subscriptions") // <2>
class SaasSubscriptionGetListController(private val repository: SaasSubscriptionRepository) { // <3>

    @GetMapping // <4>
    fun findAll(pageable: Pageable): ResponseEntity<List<SaasSubscription>> {
        val pageRequest = pageRequestOf(pageable)
        val page = repository.findAll(pageRequest)
        return ResponseEntity.ok(page.content)
    }

    private fun pageRequestOf(pageable: Pageable): PageRequest =
        PageRequest.of(
            pageable.pageNumber,
            pageable.pageSize,
            pageable.getSortOr(DEFAULT_SORT)
        )

    private companion object {
        val DEFAULT_SORT: Sort = Sort.by(Sort.Direction.ASC, "cents")
    }
}
