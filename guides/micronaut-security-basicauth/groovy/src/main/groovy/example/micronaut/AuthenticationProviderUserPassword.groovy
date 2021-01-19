package example.micronaut

import edu.umd.cs.findbugs.annotations.Nullable
import groovy.transform.CompileStatic
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.UserDetails
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import javax.inject.Singleton

@CompileStatic
@Singleton // <1>
class AuthenticationProviderUserPassword implements AuthenticationProvider { // <2>

    @Override
    Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        Flowable.create({ emitter ->
            if ( authenticationRequest.identity == "sherlock" && authenticationRequest.secret == "password" ) {
                emitter.onNext(new UserDetails((String) authenticationRequest.identity, []))
                emitter.onComplete()
            } else {
                emitter.onError(new AuthenticationException(new AuthenticationFailed()))
            }

        }, BackpressureStrategy.ERROR)
    }
}
