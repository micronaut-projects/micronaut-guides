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

import io.micronaut.core.annotation.Nullable
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpRequest
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.provider.HttpRequestReactiveAuthenticationProvider
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Flux
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import org.reactivestreams.Publisher
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.util.concurrent.ExecutorService

import static io.micronaut.security.authentication.AuthenticationFailureReason.ACCOUNT_EXPIRED
import static io.micronaut.security.authentication.AuthenticationFailureReason.ACCOUNT_LOCKED
import static io.micronaut.security.authentication.AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH
import static io.micronaut.security.authentication.AuthenticationFailureReason.PASSWORD_EXPIRED
import static io.micronaut.security.authentication.AuthenticationFailureReason.USER_DISABLED
import static io.micronaut.security.authentication.AuthenticationFailureReason.USER_NOT_FOUND

@Singleton
class DelegatingAuthenticationProvider<B> implements HttpRequestReactiveAuthenticationProvider<B> {

    private final UserFetcher userFetcher
    private final PasswordEncoder passwordEncoder
    private final AuthoritiesFetcher authoritiesFetcher
    private final Scheduler scheduler

    DelegatingAuthenticationProvider(UserFetcher userFetcher,
                                     PasswordEncoder passwordEncoder,
                                     AuthoritiesFetcher authoritiesFetcher,
                                     @Named(TaskExecutors.BLOCKING) ExecutorService executorService) { // <1>
        this.userFetcher = userFetcher
        this.passwordEncoder = passwordEncoder
        this.authoritiesFetcher = authoritiesFetcher
        this.scheduler = Schedulers.fromExecutorService(executorService)
    }

    @Override
    @NonNull
     Publisher<AuthenticationResponse> authenticate(
            @Nullable HttpRequest<B> requestContext,
            @NonNull AuthenticationRequest<String, String> authenticationRequest
    ) {
        Flux.create({ emitter ->
            UserState user = fetchUserState(authenticationRequest)
            AuthenticationFailed authenticationFailed = validate(user, authenticationRequest)
            if (authenticationFailed) {
                emitter.error(new AuthenticationException(authenticationFailed))
            } else {
                emitter.next(createSuccessfulAuthenticationResponse(user))
                emitter.complete()
            }
        }, FluxSink.OverflowStrategy.ERROR)
                .subscribeOn(scheduler) // <2>
    }

    private AuthenticationFailed validate(UserState user, AuthenticationRequest authenticationRequest) {

        AuthenticationFailed authenticationFailed = null
        if (!user) {
            authenticationFailed = new AuthenticationFailed(USER_NOT_FOUND)

        } else if (!user.enabled) {
            authenticationFailed = new AuthenticationFailed(USER_DISABLED)

        } else if (user.accountExpired) {
            authenticationFailed = new AuthenticationFailed(ACCOUNT_EXPIRED)

        } else if (user.accountLocked) {
            authenticationFailed = new AuthenticationFailed(ACCOUNT_LOCKED)

        } else if (user.passwordExpired) {
            authenticationFailed = new AuthenticationFailed(PASSWORD_EXPIRED)

        } else if (!passwordEncoder.matches(authenticationRequest.secret.toString(), user.password)) {
            authenticationFailed = new AuthenticationFailed(CREDENTIALS_DO_NOT_MATCH)
        }

        authenticationFailed
    }

    private UserState fetchUserState(AuthenticationRequest authRequest) {
        final String username = authRequest.identity
        userFetcher.findByUsername(username).orElse(null)
    }

    private AuthenticationResponse createSuccessfulAuthenticationResponse(UserState user) {
        List<String> authorities = authoritiesFetcher.findAuthoritiesByUsername(user.username)
        AuthenticationResponse.success(user.username, authorities)
    }
}
