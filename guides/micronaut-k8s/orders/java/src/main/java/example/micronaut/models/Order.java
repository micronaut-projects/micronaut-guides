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
package example.micronaut.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.Max;
import java.math.BigDecimal;
import java.util.List;

@Serdeable // <1>
public record Order(
        @Max(10000) @Nullable Integer id, // <2>
        @JsonProperty("user_id") Integer userId,
        @Nullable List<Item> items, // <3>
        @JsonProperty("item_ids") @Nullable List<Integer> itemIds, // <4>
        @Nullable BigDecimal total
) {
}