package example.micronaut

import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode
import io.micronaut.security.errors.OauthErrorResponseException
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent
import io.micronaut.security.token.refresh.RefreshTokenPersistence
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Flux
import org.reactivestreams.Publisher
import jakarta.inject.Singleton
import io.micronaut.security.authentication.Authentication

@Singleton // <1>
class CustomRefreshTokenPersistence implements RefreshTokenPersistence {
    private final RefreshTokenRepository refreshTokenRepository

    CustomRefreshTokenPersistence(RefreshTokenRepository refreshTokenRepository) {  // <2>
        this.refreshTokenRepository = refreshTokenRepository
    }

    @Override
    void persistToken(RefreshTokenGeneratedEvent event) { // <3>
        if (event?.refreshToken && event?.authentication?.name) {
            String payload = event.refreshToken
            refreshTokenRepository.save(event.authentication.name, payload, Boolean.FALSE) // <4>
        }
    }

    @Override
    Publisher<Authentication> getAuthentication(String refreshToken) {
        Flux.create(emitter -> {
            Optional<RefreshTokenEntity> tokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken)
            if (tokenOpt.isPresent()) {
                RefreshTokenEntity token = tokenOpt.get()
                if (token.getRevoked()) {
                    emitter.error(new OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "refresh token revoked", null)) // <5>
                } else {
                    emitter.next(Authentication.build(token.username)) // <6>
                    emitter.complete()
                }
            } else {
                emitter.error(new OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "refresh token not found", null)) // <7>
            }
        }, FluxSink.OverflowStrategy.ERROR) as Publisher<Authentication>
    }
}
