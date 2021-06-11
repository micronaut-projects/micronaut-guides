package example.micronaut;

import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter
import org.reactivestreams.Publisher;

import jakarta.inject.Singleton;

@Singleton // <1>
class CustomRefreshTokenPersistence : RefreshTokenPersistence {

    private var refreshTokenRepository: RefreshTokenRepository

    constructor(refreshTokenRepository: RefreshTokenRepository) {  // <2>
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @EventListener // <3>
    override fun persistToken(event: RefreshTokenGeneratedEvent?) {
        if (event?.refreshToken != null &&
            event.userDetails != null &&
            event.userDetails.username != null) {
            val payload = event.refreshToken;
            refreshTokenRepository.save(event.userDetails.username, payload, false); // <4>
        }
    }

    override fun getUserDetails(refreshToken: String) : Publisher<UserDetails> {
        return Flowable.create({ emitter: FlowableEmitter<UserDetails> ->
            val tokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (tokenOpt.isPresent) {
                val token = tokenOpt.get();
                if (token.revoked) {
                    emitter.onError(OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "refresh token revoked", null)); // <5>
                } else {
                    emitter.onNext(UserDetails(token.username, listOf())); // <6>
                    emitter.onComplete();
                }
            } else {
                emitter.onError(OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "refresh token not found", null)); // <7>
            }
        }, BackpressureStrategy.ERROR);
    }
}
