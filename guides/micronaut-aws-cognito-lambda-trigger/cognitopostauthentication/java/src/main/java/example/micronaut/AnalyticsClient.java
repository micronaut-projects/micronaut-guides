package example.micronaut;

import io.micronaut.function.client.FunctionClient;

@FunctionClient
public interface AnalyticsClient {
    void authenticated(String username);
}
