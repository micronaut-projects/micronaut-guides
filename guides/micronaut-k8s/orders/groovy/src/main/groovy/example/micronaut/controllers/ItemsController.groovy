/*
 * Copyright 2017-2023 original authors
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
package example.micronaut.controllers

import example.micronaut.models.Item
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/items")  // <1>
@Secured(SecurityRule.IS_AUTHENTICATED)  // <2>
class ItemsController {

    @Get("/{id}")  // <3>
    Item findById(int id) {
        Item.items.stream()
                .filter(it -> it.id == id)
                .findFirst().orElse(null)
    }

    @Get  // <4>
    List<Item> getItems() {
        Item.items
    }
}
