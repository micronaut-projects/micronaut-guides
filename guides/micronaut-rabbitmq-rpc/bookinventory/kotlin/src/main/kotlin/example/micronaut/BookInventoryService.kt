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

import io.micronaut.rabbitmq.annotation.Queue
import io.micronaut.rabbitmq.annotation.RabbitListener
import java.util.Optional
import jakarta.validation.constraints.NotBlank

@RabbitListener // <1>
open class BookInventoryService {

    @Queue("inventory") // <2>
    open fun stock(isbn: @NotBlank String?): Boolean? =
        bookInventoryByIsbn(isbn).map { (_, stock): BookInventory -> stock > 0 }.orElse(null)

    private fun bookInventoryByIsbn(isbn: String?): Optional<BookInventory> =
        if (isbn.equals("1491950358")) {
            Optional.of(BookInventory(isbn!!, 4))
        } else if (isbn.equals("1680502395")) {
            Optional.of(BookInventory(isbn!!, 0))
        } else {
            Optional.empty<Any>() as Optional<BookInventory>
        }
}
