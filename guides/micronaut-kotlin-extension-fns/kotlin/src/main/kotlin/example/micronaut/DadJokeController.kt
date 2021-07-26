//tag::fileHead[]
package example.micronaut

import io.micronaut.core.async.annotation.SingleResult
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
    @SingleResult
    fun getAJoke(): Publisher<String> {
        return Mono.from(dadJokeClient.tellMeAJoke()).map(DadJoke::joke)
    }
//end::standardGet[]

    //tag::usingExt[]
    @Get("/dogJokes")
    @SingleResult
    fun getDogJokes(): Publisher<List<DadJoke>> {
        return dadJokeClient.getDogJokes() // <1>
    }
//end::usingExt[]
//tag::end[]
}
//end::end[]

//tag::clientExt[]
@SingleResult
fun DadJokeClient.getDogJokes(): Publisher<List<DadJoke>> { // <1>
    return Mono.from(this.searchDadJokes("dog")).map(DadJokePagedResults::results)
}
//end::clientExt[]
