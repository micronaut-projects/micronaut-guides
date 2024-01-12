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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable // <1>
@MappedEntity // <2>
public class Pet {

    @Id // <3>
    @GeneratedValue(GeneratedValue.Type.AUTO) // <4>
    private Long id;

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @NotNull
    private PetType type = PetType.DOG;

    public Pet() {
    }

    public Pet(@NonNull String name) {
        this.name = name;
    }

    public Pet(@NonNull String name, @NonNull PetType type) {
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public PetType getType() {
        return type;
    }

    public void setType(@NonNull PetType type) {
        this.type = type;
    }
}