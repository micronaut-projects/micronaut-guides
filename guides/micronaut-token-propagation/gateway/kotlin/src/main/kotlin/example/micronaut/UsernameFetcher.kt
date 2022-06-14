package example.micronaut

import reactor.core.publisher.Mono

interface UsernameFetcher {
    fun findUsername(): Mono<String>
}
