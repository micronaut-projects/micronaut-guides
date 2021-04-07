//tag::fileHead[]
package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import javax.inject.Inject
//end::fileHead[]

//tag::start[]
@Controller("/dadJokes")
class DadJokeController {

    @Inject
    lateinit var dadJokeClient: DadJokeClient
//end::start[]

//tag::standardGet[]
    @Get("/joke")
    fun getAJoke(): String {
        return dadJokeClient.tellMeAJoke().blockingGet().joke
    }
//end::standardGet[]

//tag::usingExt[]
    @Get("/dogJokes")
    fun getDogJokes(): List<DadJoke> {
        return dadJokeClient.getDogJokes() // <1>
    }
//end::usingExt[]
//tag::end[]
}
//end::end[]

//tag::clientExt[]
fun DadJokeClient.getDogJokes(): List<DadJoke> { // <1>
    return this.searchDadJokes("dog").blockingGet().results
}
//end::clientExt[]
