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
package example.micronaut.models

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.Creator
import io.micronaut.serde.annotation.Serdeable


@CompileStatic
@EqualsAndHashCode
@Serdeable // <1>
class Item {

    Integer id
    String name
    BigDecimal price

    public static List<Item> items = [
            new Item(1, "Banana", new BigDecimal("1.5")),
            new Item(2, "Kiwi", new BigDecimal("2.5")),
            new Item(3, "Grape", new BigDecimal("1.25"))
    ]

    @Creator
    Item(Integer id,
         String name,
         BigDecimal price
    ) {
        this.id = id
        this.name = name
        this.price = price
    }
}
