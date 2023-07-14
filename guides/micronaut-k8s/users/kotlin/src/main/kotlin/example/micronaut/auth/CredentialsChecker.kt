package example.micronaut.auth

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink

@Singleton // <1>
class CredentialsChecker(private val credentials: Credentials) : AuthenticationProvider<HttpRequest<*>> {
    override fun authenticate(
        @Nullable httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {
        return Mono.create { emitter: MonoSink<AuthenticationResponse> ->
            if (authenticationRequest.identity == credentials.username && authenticationRequest.secret == credentials.password) {
                emitter.success(AuthenticationResponse.success(authenticationRequest.identity as String))
            } else {
                emitter.error(AuthenticationResponse.exception())
            }
        }
    }
}