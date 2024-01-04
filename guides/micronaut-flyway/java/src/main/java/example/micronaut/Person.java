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
/*
//tag::package[]
package example.micronaut;
//end::package[]
*/
//tag::clazz1[]
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Version;

import jakarta.validation.constraints.NotBlank;

@MappedEntity // <1>
public class Person {

    @Id // <2>
    @GeneratedValue // <3>
    private Long id;

    @Version // <4>
    private Long version;

    @NonNull
    @NotBlank
    private final String name;
    //end::clazz1[]

    //tag::nullableage[]
    @Nullable
    private final Integer age;

    public Person(@NonNull String name,
                  @Nullable Integer age) {
        this.name = name;
        this.age = age;
    }

    @Nullable
    public Integer getAge() {
        return age;
    }

    //end::nullableage[]

    /*
//tag::ageint[]
    private final int age;

    public Person(@NonNull String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }
//end::ageint[]

     */
//tag::clazz2[]

    @NonNull
    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
//end::clazz2[]
