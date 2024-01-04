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
//tag::fileHead[]

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import jakarta.inject.Inject
import reactor.core.publisher.Mono
//end::fileHead[]

//tag::start[]
@Controller("/dadJokes")
class DadJokeController {

    @Inject
    lateinit var dadJokeClient: DadJokeClient
//end::start[]

    //tag::standardGet[]
    @Get(uri = "/joke", produces = [TEXT_PLAIN])
    fun getAJoke(): Mono<String> {
        return Mono.from(dadJokeClient.tellMeAJoke()).map(DadJoke::joke)
    }
//end::standardGet[]

    //tag::usingExt[]
    @Get("/dogJokes")
    fun getDogJokes(): Mono<List<DadJoke>> {
        return dadJokeClient.getDogJokes() // <1>
    }
//end::usingExt[]
//tag::end[]
}
//end::end[]

//tag::clientExt[]
fun DadJokeClient.getDogJokes(): Mono<List<DadJoke>> { // <1>
    return Mono.from(this.searchDadJokes("dog")).map(DadJokePagedResults::results)
}
//end::clientExt[]
