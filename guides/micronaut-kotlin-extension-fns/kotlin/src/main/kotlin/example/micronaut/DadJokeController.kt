//tag::fileHead[]
package example.micronaut

import reactor.core.publisher.Mono
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import jakarta.inject.Inject
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

//end::fileHead[]

//tag::start[]
@Controller("/dadJokes")
class DadJokeController {

    @Inject
    lateinit var dadJokeClient: DadJokeClient
//end::start[]

    //tag::standardGet[]
    @Get("/joke")
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
