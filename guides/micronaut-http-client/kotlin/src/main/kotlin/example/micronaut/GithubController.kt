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

import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.reactivestreams.Publisher

@Controller("/github") // <1>
class GithubController(private val githubLowLevelClient: GithubLowLevelClient, // <2>
                       private val githubApiClient: GithubApiClient) {

    @Get("/releases-lowlevel") // <3>
    @SingleResult // <4>
    fun releasesWithLowLevelClient(): Publisher<List<GithubRelease>> {
        return githubLowLevelClient.fetchReleases()
    }

    @Get("/releases") // <5>
    @SingleResult // <4>
    fun fetchReleases(): Publisher<List<GithubRelease>> {
        return githubApiClient.fetchReleases()
    }
}
