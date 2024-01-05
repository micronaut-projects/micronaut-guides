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
package example.micronaut

import example.micronaut.models.Item
import example.micronaut.models.Order
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("/") // <1>
interface OrderItemClient {
    @Get("/orders/{id}")
    fun getOrderById(@Header authorization: String?, id: Int?): Order?

    @Post("/orders")
    fun createOrder(@Header authorization: String?, @Body order: Order?): Order

    @Get("/orders")
    fun getOrders(@Header authorization: String?): List<Order>

    @Get("/items")
    fun getItems(@Header authorization: String?): List<Item>

    @Get("/items/{id}")
    fun getItemsById(@Header authorization: String?, id: Int?): Item?
}