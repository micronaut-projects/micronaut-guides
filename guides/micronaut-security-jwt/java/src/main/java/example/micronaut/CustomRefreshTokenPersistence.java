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
import java.util.ArrayList;
import java.util.Optional;

@Singleton // <1>
public class CustomRefreshTokenPersistence implements RefreshTokenPersistence {
    private final RefreshTokenRepository refreshTokenRepository;

    public CustomRefreshTokenPersistence(RefreshTokenRepository refreshTokenRepository) {  // <2>
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void persistToken(RefreshTokenGeneratedEvent event) { // <3>
        if (event != null &&
                event.getRefreshToken() != null &&
                event.getUserDetails() != null &&
                event.getUserDetails().getUsername() != null) {
            String payload = event.getRefreshToken();
            refreshTokenRepository.save(event.getUserDetails() .getUsername(), payload, Boolean.FALSE); // <4>
        }
    }

    @Override
    public Publisher<UserDetails> getUserDetails(String refreshToken) {
        return Flux.create(emitter -> {
            Optional<RefreshTokenEntity> tokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (tokenOpt.isPresent()) {
                RefreshTokenEntity token = tokenOpt.get();
                if (token.getRevoked()) {
                    emitter.error(new OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "refresh token revoked", null)); // <5>
                } else {
                    emitter.next(new UserDetails(token.getUsername(), new ArrayList<>())); // <6>
                    emitter.complete();
                }
            } else {
                emitter.error(new OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT, "refresh token not found", null)); // <7>
            }
        }, FluxSink.OverflowStrategy.ERROR);

    }
}
