package example.micronaut

import io.micronaut.security.authentication.Authentication
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode.INVALID_GRANT
import io.micronaut.security.errors.OauthErrorResponseException
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent
import io.micronaut.security.token.refresh.RefreshTokenPersistence
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

@Singleton // <1>
class CustomRefreshTokenPersistence(private val refreshTokenRepository: RefreshTokenRepository) // <2>
    : RefreshTokenPersistence {

    override fun persistToken(event: RefreshTokenGeneratedEvent?) { // <3>
        if (event?.refreshToken != null && event.authentication?.name != null) {
            val payload = event.refreshToken
            refreshTokenRepository.save(event.authentication.name, payload, false) // <4>
        }
    }

    override fun getAuthentication(refreshToken: String): Publisher<Authentication> {
        return Flux.create({ emitter: FluxSink<Authentication> ->
            val tokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken)
            if (tokenOpt.isPresent) {
                val (_, username, _, revoked) = tokenOpt.get()
                if (revoked) {
                    emitter.error(OauthErrorResponseException(INVALID_GRANT, "refresh token revoked", null)) // <5>
                } else {
                    emitter.next(Authentication.build(username)) // <6>
                    emitter.complete()
                }
            } else {
                emitter.error(OauthErrorResponseException(INVALID_GRANT, "refresh token not found", null)) // <7>
            }
        }, FluxSink.OverflowStrategy.ERROR)
    }
}
