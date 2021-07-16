package example.micronaut

import io.micronaut.core.annotation.Nullable
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
class AuthenticationProviderUserPassword implements AuthenticationProvider { // <2>

    @Override
    Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        Flux.create(emitter -> {
            if (authenticationRequest.identity == "sherlock" && authenticationRequest.secret == "password") {
                emitter.next(new UserDetails((String) authenticationRequest.identity, []))
                emitter.complete()
            } else {
                emitter.error(new AuthenticationException(new AuthenticationFailed()))
            }
        }, FluxSink.OverflowStrategy.ERROR) as Publisher<AuthenticationResponse>
    }
}
