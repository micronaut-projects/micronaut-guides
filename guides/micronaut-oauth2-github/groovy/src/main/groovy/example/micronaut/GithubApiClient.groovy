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
//end::package[]
*/
//tag::clazz[]

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher

import jakarta.validation.constraints.Pattern

@CompileStatic
@Header(name = 'User-Agent', value = 'https://micronautguides.com')
@Header(name = 'Accept', value = 'application/vnd.github.v3+json, application/json') // <1>
@Client(id = 'githubv3') // <2>
interface GithubApiClient {

    @Get('/user') // <3>
    Publisher<GithubUser> getUser( // <4>
            @Header(HttpHeaders.AUTHORIZATION) String authorization) // <5>
    //end::clazz[]
    //tag::repos[]
    @Get('/user/repos{?sort,direction}') // <1>
    List<GithubRepo> repos(
            @Pattern(regexp = 'created|updated|pushed|full_name') @Nullable @QueryValue String sort, // <2>
            @Pattern(regexp = 'asc|desc') @Nullable @QueryValue String direction, // <2>
            @Header(HttpHeaders.AUTHORIZATION) String authorization)
    //end::repos[]
//tag::clazz[]
}
//end::clazz[]
