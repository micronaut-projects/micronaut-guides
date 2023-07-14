package example.micronaut.advanced.auth;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Singleton
class AuthProvider implements AuthenticationProvider<HttpRequest<?>> {

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Mono.create(it -> it.success(AuthenticationResponse.success(authenticationRequest.getIdentity().toString())));
    }
}