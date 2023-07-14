package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

@Singleton // <1>
class AuthenticationProviderUserPassword : AuthenticationProvider<HttpRequest<*>> { // <2>
    override fun authenticate(httpRequest: HttpRequest<*>?,
                              authenticationRequest: AuthenticationRequest<*, *>): Publisher<AuthenticationResponse> {
        return Flux.create({ emitter: FluxSink<AuthenticationResponse> ->
            if (authenticationRequest.identity == "sherlock" && authenticationRequest.secret == "password") {
                emitter.next(AuthenticationResponse.success(authenticationRequest.identity as String))
                emitter.complete()
            } else {
                emitter.error(AuthenticationResponse.exception())
            }
        }, FluxSink.OverflowStrategy.ERROR)
    }
}
