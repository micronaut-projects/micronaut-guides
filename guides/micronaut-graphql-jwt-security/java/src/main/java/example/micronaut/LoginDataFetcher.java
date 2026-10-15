/*
 * Copyright 2017-2026 original authors
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
package example.micronaut;

import graphql.GraphQLContext;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.Authenticator;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.handlers.LoginHandler;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;

@Singleton
public class LoginDataFetcher implements DataFetcher<LoginPayload> {

    private final Authenticator<HttpRequest<?>> authenticator;
    private final LoginHandler<HttpRequest<?>, MutableHttpResponse<?>> loginHandler;
    private final UserRepository userRepository;

    public LoginDataFetcher(Authenticator<HttpRequest<?>> authenticator,
                            LoginHandler<HttpRequest<?>, MutableHttpResponse<?>> loginHandler,
                            UserRepository userRepository) {
        this.authenticator = authenticator;
        this.loginHandler = loginHandler;
        this.userRepository = userRepository;
    }

    @Override
    public LoginPayload get(DataFetchingEnvironment environment) {
        GraphQLContext graphQLContext = environment.getGraphQlContext();
        HttpRequest<?> httpRequest = graphQLContext.get("httpRequest");
        MutableHttpResponse<?> httpResponse = graphQLContext.get("httpResponse");

        String username = environment.getArgument("username");
        String password = environment.getArgument("password");

        AuthenticationResponse authenticationResponse = Flux.from(
                authenticator.authenticate(httpRequest, new UsernamePasswordCredentials(username, password))
        ).blockFirst();

        if (authenticationResponse != null &&
                authenticationResponse.isAuthenticated() &&
                authenticationResponse.getAuthentication().isPresent()) {
            Authentication authentication = authenticationResponse.getAuthentication().get();
            MutableHttpResponse<?> loginResponse = loginHandler.loginSuccess(authentication, httpRequest);
            if (httpResponse != null) {
                loginResponse.getCookies().forEach(httpResponse::cookie);
            }
            return userRepository.findByUsername(username)
                    .map(LoginPayload::ofUser)
                    .orElseGet(() -> LoginPayload.ofError("User not found"));
        }

        String error = authenticationResponse != null
                ? authenticationResponse.getMessage().orElse("Invalid credentials")
                : "Invalid credentials";
        return LoginPayload.ofError(error);
    }
}
