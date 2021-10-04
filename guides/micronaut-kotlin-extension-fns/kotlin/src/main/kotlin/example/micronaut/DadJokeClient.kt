package example.micronaut

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono

@Client("https://icanhazdadjoke.com/")
@Header(name = "Accept", value = "application/json")
interface DadJokeClient {

    @Get
    fun tellMeAJoke(): Mono<DadJoke>

    @Get("/search?term={searchTerm}")
    fun searchDadJokes(@QueryValue searchTerm: String): Mono<DadJokePagedResults>
}
