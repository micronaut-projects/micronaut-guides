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
//tag::clazz[]
package example.micronaut

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Headers
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher
import jakarta.validation.constraints.Pattern

@Headers(
    Header(name = "User-Agent", value = "https://micronautguides.com"),
    Header(name = "Accept", value = "application/vnd.github.v3+json, application/json") // <1>
)
@Client(id = "githubv3") // <2>
interface GithubApiClient {

    @Get("/user") // <3>
    fun getUser(@Header(HttpHeaders.AUTHORIZATION) // <5>
                authorization: String?): Publisher<GithubUser> // <4>
    //end::clazz[]
    //tag::repos[]
    @Get("/user/repos{?sort,direction}") // <1>
    fun repos(
            @Pattern(regexp = "created|updated|pushed|full_name") @Nullable @QueryValue sort: String?, // <2>
            @Pattern(regexp = "asc|desc") @Nullable @QueryValue direction: String?, // <2>
            @Header(HttpHeaders.AUTHORIZATION) authorization: String?): List<GithubRepo>
    //end::repos[]
//tag::clazz[]
}
//end::clazz[]
