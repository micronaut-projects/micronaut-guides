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

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders.ACCEPT
import io.micronaut.http.HttpHeaders.USER_AGENT
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import java.net.URI

@Singleton // <1>
class GithubLowLevelClient(@param:Client(id = "github") private val httpClient: HttpClient,  // <2>
                           configuration: GithubConfiguration) {  // <3>
    private val uri: URI = UriBuilder.of("/repos")
        .path(configuration.organization)
        .path(configuration.repo)
        .path("releases")
        .build()

    fun fetchReleases(): Publisher<List<GithubRelease>> {
        val req: HttpRequest<*> = HttpRequest.GET<Any>(uri) // <4>
            .header(USER_AGENT, "Micronaut HTTP Client") // <5>
            .header(ACCEPT, "application/vnd.github.v3+json, application/json") // <6>
        return httpClient.retrieve(req, Argument.listOf(GithubRelease::class.java)) // <7>
    }
}
