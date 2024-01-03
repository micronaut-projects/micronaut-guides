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

import example.micronaut.models.Item;
import example.micronaut.models.Order;
import example.micronaut.models.User;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client("/") // <1>
public interface GatewayClient {

    @Get("/api/items/{id}")
    Item getItemById(int id);

    @Get("/api/orders/{id}")
    Order getOrderById(int id);

    @Get("/api/users/{id}")
    User getUsersById(int id);

    @Get("/api/users")
    List<User> getUsers();

    @Get("/api/items")
    List<Item> getItems();

    @Get("/api/orders")
    List<Order> getOrders();

    @Post("/api/orders")
    Order createOrder(@Body Order order);

    @Post("/api/users")
    User createUser(@Body User user);
}
