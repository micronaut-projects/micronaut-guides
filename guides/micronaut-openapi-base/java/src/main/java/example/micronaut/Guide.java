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

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Serdeable // <1>
public record Guide(@NotBlank String title,
                    @NotBlank String intro,
                    @NotNull @Size(min = 1) List<@NotBlank String> authors, // <2>
                    @Nullable List<@NotBlank String> tags,
                    @NotNull @Size(min = 1) List<@NotBlank String> categories,
                    @Schema(format = "yyyy-MM-dd", example = "2018-05-23") @NotNull LocalDate publicationDate, // <3>
                    @NotBlank String slug,
                    @NotBlank String url,
                    @NotNull @Size(min = 1) List<@NotNull @Valid Option> options) {
}
