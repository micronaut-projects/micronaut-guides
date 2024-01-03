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
package example.micronaut.controllers

import example.micronaut.models.Item
import example.micronaut.models.Order
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

import jakarta.validation.Valid

@Controller("/orders")  // <1>
@Secured(SecurityRule.IS_AUTHENTICATED)  // <2>
class OrdersController {

    private final List<Order> orders = []

    @Get("/{id}")  // <3>
    Order findById(int id) {
        orders.stream()
                .filter(it -> it.id == id)
                .findFirst().orElse(null)
    }

    @Get  // <4>
    List<Order> getOrders() {
        orders
    }

    @Post  // <5>
    Order createOrder(@Body @Valid Order order) {
        if (order.itemIds == null || order.itemIds.isEmpty()) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Items must be supplied")
        }

        def items = order.itemIds.stream().map(
                x -> Item.items.stream().filter(
                        y -> y.id == x
                ).findFirst().orElseThrow(
                        () -> new HttpStatusException(HttpStatus.BAD_REQUEST, String.format("Item with id %s doesn't exist", x))
                )).collect()

        BigDecimal total = items.stream().map(x->x.price).reduce(BigDecimal::add).orElse(new BigDecimal("0"))
        Order newOrder = new Order(orders.size() + 1, order.userId, items, null, total)

        orders.add(newOrder)
        newOrder
    }
}
