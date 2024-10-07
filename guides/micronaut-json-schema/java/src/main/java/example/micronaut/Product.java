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


import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.jsonschema.JsonSchema;
import jakarta.validation.constraints.*;

import java.util.Set;

@JsonSchema(description = "A product from Acme's catalog") // <1>
public record Product(
        @NotNull // <2>
        @JsonPropertyDescription("The unique identifier for a product") // <3>
        Long productId,

        @NonNull
        @NotBlank
        @JsonPropertyDescription("Name of the product") // <3>
        String productName,

        @NotNull // <1>
        @JsonPropertyDescription("The price of the product") // <3>
        @Positive // <4>
        Double price,

        @Size(min = 1) // <4>
        Set<String> tags
) {
}
