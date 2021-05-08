package example.micronaut

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.UserDetails
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

import javax.inject.Named
import javax.inject.Singleton
import java.util.concurrent.ExecutorService

@Singleton
class DelegatingAuthenticationProvider implements AuthenticationProvider {

    protected final UserFetcher userFetcher
    protected final PasswordEncoder passwordEncoder
    protected final AuthoritiesFetcher authoritiesFetcher
    protected final Scheduler scheduler

    DelegatingAuthenticationProvider(UserFetcher userFetcher,
                                     PasswordEncoder passwordEncoder,
                                     AuthoritiesFetcher authoritiesFetcher,
                                     @Named(TaskExecutors.IO) ExecutorService executorService) { // <1>
        this.userFetcher = userFetcher
        this.passwordEncoder = passwordEncoder
        this.authoritiesFetcher = authoritiesFetcher
        this.scheduler = Schedulers.from(executorService)
    }

    @Override
    Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest,
                                                   AuthenticationRequest<?, ?> authenticationRequest) {
        Flowable.create({ emitter ->
            UserState user = fetchUserState(authenticationRequest)
            Optional<AuthenticationFailed> authenticationFailed = validate(user, authenticationRequest)
            if (authenticationFailed.isPresent()) {
                emitter.onError(new AuthenticationException(authenticationFailed.get()))
            } else {
                emitter.onNext(createSuccessfulAuthenticationResponse(authenticationRequest, user))
            }
            emitter.onComplete()
        }, BackpressureStrategy.ERROR)
                .subscribeOn(scheduler) // <2>
    }

    protected Optional<AuthenticationFailed> validate(UserState user, AuthenticationRequest authenticationRequest) {

        AuthenticationFailed authenticationFailed = null
        if (user == null) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND)

        } else if (!user.isEnabled()) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.USER_DISABLED)

        } else if (user.isAccountExpired()) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.ACCOUNT_EXPIRED)

        } else if (user.isAccountLocked()) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.ACCOUNT_LOCKED)

        } else if (user.isPasswordExpired()) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.PASSWORD_EXPIRED)

        } else if (!passwordEncoder.matches(authenticationRequest.getSecret().toString(), user.getPassword())) {
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
        }
        Optional.ofNullable(authenticationFailed)
    }

    protected UserState fetchUserState(AuthenticationRequest authenticationRequest) {
        final String username = authenticationRequest.getIdentity().toString()
        userFetcher.findByUsername(username)
    }

    protected AuthenticationResponse createSuccessfulAuthenticationResponse(AuthenticationRequest authenticationRequest, UserState user) {
        List<String> authorities = authoritiesFetcher.findAuthoritiesByUsername(user.getUsername())
        new UserDetails(user.getUsername(), authorities)
    }
}
