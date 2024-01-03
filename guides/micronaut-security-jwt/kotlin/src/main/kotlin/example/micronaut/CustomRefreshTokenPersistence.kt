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
