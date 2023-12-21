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
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import jakarta.validation.constraints.NotNull

@Controller("/items") // <1>
@Secured(SecurityRule.IS_AUTHENTICATED) // <2>
open class ItemsController {

    @Get("/{id}") // <3>
    open fun findById(id: @NotNull Int?): Item? {
        return Item.items
            .firstOrNull { it.id == id }
    }

    @Get // <4>
    fun getItems(): List<Item?> {
        return Item.items
    }
}