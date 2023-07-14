package example.micronaut.advanced.auth

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

@Singleton
class AuthProvider : AuthenticationProvider<HttpRequest<*>> {

    override fun authenticate(
        @Nullable httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> =
        Mono.create { it.success(AuthenticationResponse.success(authenticationRequest.identity.toString())) }
}