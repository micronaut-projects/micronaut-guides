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
import example.micronaut.models.Item.Companion.items
import example.micronaut.models.Order
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.math.BigDecimal
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

@Controller("/orders") // <1>
@Secured(SecurityRule.IS_AUTHENTICATED) // <2>
open class OrdersController {

    private val orders: MutableList<Order> = ArrayList()

    @Get("/{id}") // <3>
    open fun findById(id: @NotNull Int?): Order? {
        return orders.firstOrNull{ it: Order ->
            it.id == id
        }
    }

    @Get // <4>
    fun getOrders(): List<Order?> {
        return orders
    }

    @Post // <5>
    open fun createOrder(@Body order: @Valid Order): Order {
        if (order.itemIds.isNullOrEmpty()) {
            throw HttpStatusException(HttpStatus.BAD_REQUEST, "Items must be supplied")
        }
        val items: List<Item> = order.itemIds.map { x ->
            items.firstOrNull { y: Item ->
                y.id == x
            }?: throw HttpStatusException(HttpStatus.BAD_REQUEST, String.format("Item with id %s doesn't exist", x))
        }
        val total: BigDecimal = items.map(Item::price).reduce(BigDecimal::add)
        val newOrder = Order(orders.size + 1, order.userId, items, null, total)
        orders.add(newOrder)
        return newOrder
    }

}