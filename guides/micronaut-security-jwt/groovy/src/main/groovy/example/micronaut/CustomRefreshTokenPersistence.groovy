/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut

import io.micronaut.security.authentication.Authentication
import io.micronaut.security.errors.OauthErrorResponseException
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent
import io.micronaut.security.token.refresh.RefreshTokenPersistence
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

import static io.micronaut.security.errors.IssuingAnAccessTokenErrorCode.INVALID_GRANT

@Singleton // <1>
class CustomRefreshTokenPersistence implements RefreshTokenPersistence {

    private final RefreshTokenRepository refreshTokenRepository

    CustomRefreshTokenPersistence(RefreshTokenRepository refreshTokenRepository) { // <2>
        this.refreshTokenRepository = refreshTokenRepository
    }

    @Override
    void persistToken(RefreshTokenGeneratedEvent event) { // <3>
        if (event?.refreshToken && event?.authentication?.name) {
            String payload = event.refreshToken
            refreshTokenRepository.save(event.authentication.name, payload, false) // <4>
        }
    }

    @Override
    Publisher<Authentication> getAuthentication(String refreshToken) {
        Flux.create(emitter -> {
            Optional<RefreshTokenEntity> tokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken)
            if (tokenOpt.isPresent()) {
                RefreshTokenEntity token = tokenOpt.get()
                if (token.getRevoked()) {
                    emitter.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token revoked", null)) // <5>
                } else {
                    emitter.next(Authentication.build(token.username)) // <6>
                    emitter.complete()
                }
            } else {
                emitter.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token not found", null)) // <7>
            }
        }, FluxSink.OverflowStrategy.ERROR)
    }
}
