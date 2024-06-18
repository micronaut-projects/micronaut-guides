package io.micronaut.guides.feature;

import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.TEST;

@Singleton
public class JsonSmart extends AbstractFeature {

    public JsonSmart() {
        super("json-smart", TEST);
    }

    @Override
    public @Nullable String getThirdPartyDocumentation() {
        return "https://netplex.github.io/json-smart/";
    }
}