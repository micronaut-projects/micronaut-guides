package example.micronaut

import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.security.authentication.UserDetails
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode
import io.micronaut.security.errors.OauthErrorResponseException
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent
import io.micronaut.security.token.refresh.RefreshTokenPersistence
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import jakarta.inject.Singleton

@Singleton // <1>
class CustomRefreshTokenPersistence implements RefreshTokenPersistence {
    private final RefreshTokenRepository refreshTokenRepository

    CustomRefreshTokenPersistence(RefreshTokenRepository refreshTokenRepository) {  // <2>
        this.refreshTokenRepository = refreshTokenRepository
    }

    @Override
    @EventListener // <3>
    void persistToken(RefreshTokenGeneratedEvent event) {
        if (event?.refreshToken && event?.userDetails?.username) {
            String payload = event.refreshToken
            refreshTokenRepository.save(event.userDetails.username, payload, Boolean.FALSE) // <4>
        }
    }

    @Override
    Publisher<UserDetails> getUserDetails(String refreshToken) {
        Flowable.create(emitter -> {
            Optional<RefreshTokenEntity> tokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken)
            if (tokenOpt.isPresent()) {
                RefreshTokenEntity token = tokenOpt.get()
                if (token.getRevoked()) {
                    emitter.onError(new OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "refresh token revoked", null)) // <5>
                } else {
                    emitter.onNext(new UserDetails(token.username, [])) // <6>
                    emitter.onComplete()
                }
            } else {
                emitter.onError(new OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "refresh token not found", null)) // <7>
            }
        }, BackpressureStrategy.ERROR) as Publisher<UserDetails>
    }
}
