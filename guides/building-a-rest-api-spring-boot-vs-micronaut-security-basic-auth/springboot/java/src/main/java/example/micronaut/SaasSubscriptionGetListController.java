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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController // <1>
@RequestMapping("/subscriptions") // <2>
public class SaasSubscriptionGetListController {

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "cents");
    private final SaasSubscriptionRepository repository;

    private SaasSubscriptionGetListController(SaasSubscriptionRepository repository) { // <3>
        this.repository = repository;
    }

    @GetMapping // <4>
    private ResponseEntity<List<SaasSubscription>> findAll(Pageable pageable, Principal principal) {
        PageRequest pageRequest = pageRequestOf(pageable);
        Page<SaasSubscription> page = repository.findByOwner(principal.getName(), pageRequest);
        return ResponseEntity.ok(page.getContent());
    }

    private static PageRequest pageRequestOf(Pageable pageable) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(DEFAULT_SORT)
        );
    }
}
