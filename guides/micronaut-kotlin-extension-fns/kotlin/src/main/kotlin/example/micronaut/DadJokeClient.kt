package example.micronaut

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher
import io.micronaut.core.async.annotation.SingleResult

@Client("https://icanhazdadjoke.com/")
@Header(name = "Accept", value = "application/json")
interface DadJokeClient {
    @Get
    @SingleResult
    fun tellMeAJoke(): Publisher<DadJoke>

    @Get("/search?term={searchTerm}")
    fun searchDadJokes(@QueryValue searchTerm: String): Single<DadJokePagedResults>
}
