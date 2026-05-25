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

import com.fasterxml.jackson.annotation.JsonPropertyDescription
import io.micronaut.core.annotation.NonNull
import io.micronaut.jsonschema.JsonSchema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

@JsonSchema(description = "A product from Acme's catalog") // <2>
class Product {

    @NotNull // <3>
    @JsonPropertyDescription("The unique identifier for a product") // <1>
    Long productId

    @NonNull
    @NotBlank
    @JsonPropertyDescription("Name of the product")
    String productName

    @NotNull // <3>
    @Positive // <4>
    @JsonPropertyDescription("The price of the product")
    Double price

    @Size(min = 1) // <4>
    @JsonPropertyDescription("Product tags")
    Set<String> tags
}
