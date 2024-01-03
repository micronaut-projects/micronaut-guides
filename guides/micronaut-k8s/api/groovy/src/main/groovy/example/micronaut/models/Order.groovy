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

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@CompileStatic
@EqualsAndHashCode
@Serdeable // <1>
class Order {
    @Nullable Integer id // <2>

    @NotBlank @Nullable @JsonProperty("user_id") Integer userId

    @Nullable User user

    @Nullable List<Item> items // <3>

    @NotBlank @Nullable @JsonProperty("item_ids") List<Integer> itemIds // <4>

    @Nullable BigDecimal total

    @Creator
    Order(Integer id,
          @NotBlank @Nullable @JsonProperty("user_id") Integer userId,
          @Nullable User user,
          @Nullable List<Item> items,
          @NotBlank @JsonProperty("item_ids")
          @Nullable List<Integer> itemIds,
          @Nullable BigDecimal total
    ) {
        this.id = id
        this.userId = userId
        this.user = user
        this.items = items
        this.itemIds = itemIds
        this.total = total
    }

}
