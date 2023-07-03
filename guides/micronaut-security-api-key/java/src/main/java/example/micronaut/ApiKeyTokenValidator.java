package example.micronaut;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.validator.TokenValidator;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

@Singleton // <1>
class ApiKeyTokenValidator implements TokenValidator<HttpRequest<?>>  {

    private final ApiKeyRepository apiKeyRepository;

    ApiKeyTokenValidator(ApiKeyRepository apiKeyRepository) { // <2>
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public Publisher<Authentication> validateToken(String token, HttpRequest<?> request) {
        if (request == null || !request.getPath().startsWith("/api")) { // <3>
            return Publishers.empty();
        }
        return apiKeyRepository.findByApiKey(token)
                .map(principal -> Authentication.build(principal.getName()))
                .map(Publishers::just).orElseGet(Publishers::empty);
    }
}
