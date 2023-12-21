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
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank

@Serdeable // <1>
data class Order (
    @Nullable @Max(10000) val id:Int, // <2>
    @NotBlank @Nullable @JsonProperty("user_id") val userId:Int?,
    @Nullable val user: User?,
    val items: List<Item>?, // <3>
    @NotBlank @JsonProperty("item_ids") val itemIds:List<Int>?, // <4>
    val total: BigDecimal?)