package example.micronaut.auth

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

@Singleton // <1>
class CredentialsChecker implements AuthenticationProvider<HttpRequest<?>> {

    @Inject
    private Credentials credentials

    @Override
    Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Mono.<AuthenticationResponse>create(emitter -> {
            if ( authenticationRequest.getIdentity() == credentials.username &&
                    authenticationRequest.getSecret() == credentials.password) {
                emitter.success(AuthenticationResponse.success((String) authenticationRequest.getIdentity()));
            } else {
                emitter.error(AuthenticationResponse.exception())
            }
        })
    }
}
