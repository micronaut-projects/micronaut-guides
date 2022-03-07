package example.micronaut

import reactor.core.publisher.Mono

interface UsernameFetcher {
    Mono<String> findUsername()
}
