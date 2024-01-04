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
package example.micronaut
/*
//tag::package[]
package example.micronaut
//tag::package[]
*/
//tag::clazzwithoutsettersandgetters[]

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant

@CompileStatic
@MappedEntity // <1>
class RefreshTokenEntity {

    @Id // <2>
    @GeneratedValue // <3>
    @NonNull
    Long id

    @NonNull
    @NotBlank
    String username

    @NonNull
    @NotBlank
    String refreshToken

    @NonNull
    @NotNull
    Boolean revoked

    @DateCreated // <4>
    @NonNull
    @NotNull
    Instant dateCreated
    //end::clazzwithoutsettersandgetters[]
//tag::endclass[]
}
//end::endclass[]
