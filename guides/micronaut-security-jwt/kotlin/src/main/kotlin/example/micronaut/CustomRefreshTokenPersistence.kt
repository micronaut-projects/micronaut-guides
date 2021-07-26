package example.micronaut;

import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Flux;
import org.reactivestreams.Publisher;

import jakarta.inject.Singleton;

@Singleton // <1>
class CustomRefreshTokenPersistence : RefreshTokenPersistence {

    private var refreshTokenRepository: RefreshTokenRepository

    constructor(refreshTokenRepository: RefreshTokenRepository) {  // <2>
        this.refreshTokenRepository = refreshTokenRepository;
    }

    override fun persistToken(event: RefreshTokenGeneratedEvent?) { // <3>
        if (event?.refreshToken != null &&
            event.userDetails != null &&
            event.userDetails.username != null) {
            val payload = event.refreshToken;
            refreshTokenRepository.save(event.userDetails.username, payload, false); // <4>
        }
    }

    override fun getUserDetails(refreshToken: String) : Publisher<UserDetails> {
        return Flux.create({ emitter: FluxSink<UserDetails> ->
            val tokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (tokenOpt.isPresent) {
                val token = tokenOpt.get();
                if (token.revoked) {
                    emitter.error(OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "refresh token revoked", null)); // <5>
                } else {
                    emitter.next(UserDetails(token.username, listOf())); // <6>
                    emitter.complete();
                }
            } else {
                emitter.error(OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "refresh token not found", null)); // <7>
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }
}
