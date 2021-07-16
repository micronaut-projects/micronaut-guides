package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.UserDetails
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Flux
import org.reactivestreams.Publisher
import jakarta.inject.Singleton

@Singleton // <1>
class AuthenticationProviderUserPassword : AuthenticationProvider { // <2>

    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {
        return Flux.create({ emitter: FluxSink<AuthenticationResponse> ->
            if (authenticationRequest.identity == "sherlock" && authenticationRequest.secret == "password") {
                emitter.next(UserDetails(authenticationRequest.identity as String, ArrayList()))
                emitter.complete()
            } else {
                emitter.error(AuthenticationException(AuthenticationFailed()))
            }
        }, FluxSink.OverflowStrategy.ERROR)
    }
}
