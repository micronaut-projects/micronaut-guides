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

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.oracledb.microtx.xa.XA
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.transaction.Transactional

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/orders")
open class OrderController(
    private val orders: PurchaseOrderRepository,
    private val stock: StockClient
) {

    @XA // <1>
    @Transactional // <2>
    @Post
    @Status(HttpStatus.CREATED)
    open fun place(@Body request: OrderRequest): PurchaseOrder {
        val order = orders.save(PurchaseOrder(null, request.item, request.quantity)) // <3>
        stock.reserve(request.item, request.quantity) // <4>
        return order // <5>
    }
}
