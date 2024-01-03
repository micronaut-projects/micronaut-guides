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
package example.micronaut.models;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.util.List;

@Serdeable // <1>
public record Item(
        Integer id,
        String name,
        BigDecimal price
) {
    public static List<Item> items = List.of(
            new Item(1, "Banana", new BigDecimal("1.5")),
            new Item(2, "Kiwi", new BigDecimal("2.5")),
            new Item(3, "Grape", new BigDecimal("1.25"))
        );
}
