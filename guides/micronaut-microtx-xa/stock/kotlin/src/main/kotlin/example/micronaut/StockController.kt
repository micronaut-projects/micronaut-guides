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
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.oracledb.microtx.xa.XA
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

@ExecuteOn(TaskExecutors.BLOCKING) // <1>
@Controller("/stock")
open class StockController(private val stock: StockRepository) {

    @XA // <2>
    @Post("/{item}/reserve")
    open fun reserve(item: String, @QueryValue quantity: Int) {
        if (stock.reserve(item, quantity) == 0L) { // <3>
            throw HttpStatusException(HttpStatus.CONFLICT, "Not enough $item in stock") // <4>
        }
    }

    @Get("/{item}")
    open fun find(item: String): Stock? = stock.findById(item).orElse(null)
}
