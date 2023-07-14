package example.micronaut.auth;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Singleton // <1>
class CredentialsChecker implements AuthenticationProvider<HttpRequest<?>> {

    private final Credentials credentials;

    CredentialsChecker(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest,
                                                          AuthenticationRequest<?, ?> authenticationRequest) {
        return Mono.<AuthenticationResponse>create(emitter -> {
            if ( authenticationRequest.getIdentity().equals(credentials.username()) &&
                    authenticationRequest.getSecret().equals(credentials.password()) ) {
                emitter.success(AuthenticationResponse.success((String) authenticationRequest.getIdentity()));
            } else {
                emitter.error(AuthenticationResponse.exception());
            }
        });
    }
}
