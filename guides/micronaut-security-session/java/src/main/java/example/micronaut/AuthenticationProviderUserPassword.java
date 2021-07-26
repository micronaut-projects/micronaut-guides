package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Flux;
import org.reactivestreams.Publisher;

import jakarta.inject.Singleton;
import java.util.ArrayList;

@Singleton // <1>
public class AuthenticationProviderUserPassword implements AuthenticationProvider { // <2>

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flux.create(emitter -> {
            if (authenticationRequest.getIdentity().equals("sherlock") &&
                    authenticationRequest.getSecret().equals("password")) {
                UserDetails userDetails = new UserDetails((String) authenticationRequest.getIdentity(), new ArrayList<>());
                emitter.next(userDetails);
                emitter.complete();
            } else {
                emitter.error(new AuthenticationException(new AuthenticationFailed()));
            }

        }, FluxSink.OverflowStrategy.ERROR);
    }
}
