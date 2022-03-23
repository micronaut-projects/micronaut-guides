package example.micronaut

import io.micronaut.http.annotation.Header
import reactor.core.publisher.Mono

interface UsernameFetcher {
    fun findUsername(@Header("Authorization") authorization: String): Mono<String>
}
