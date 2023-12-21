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
package example.micronaut.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal

@Serdeable // <1>
data class Item (
    val id:Int,
    val name:String,
    val price: BigDecimal
) {
    companion object {
        var items: List<Item> = listOf(
            Item(1, "Banana", BigDecimal("1.5")),
            Item(2, "Kiwi", BigDecimal("2.5")),
            Item(3, "Grape", BigDecimal("1.25"))
        )
    }
}