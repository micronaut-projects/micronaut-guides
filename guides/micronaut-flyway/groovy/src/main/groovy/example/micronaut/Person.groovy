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
//tag::clazz1[]
package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Version

import jakarta.validation.constraints.NotBlank

@CompileStatic
@MappedEntity // <1>
class Person {

    @Id // <2>
    @GeneratedValue // <3>
    Long id

    @Version // <4>
    Long version

    @NonNull
    @NotBlank
    final String name
    //end::clazz1[]

    //tag::nullableage[]
    @Nullable
    final Integer age

    Person(@NonNull String name,
           @Nullable Integer age) {
        this.name = name
        this.age = age
    }

    //end::nullableage[]

    /*
//tag::ageint[]
    final int age

    Person(@NonNull String name, int age) {
        this.name = name
        this.age = age
    }
//end::ageint[]
     */
//tag::clazz2[]
}
//end::clazz2[]
