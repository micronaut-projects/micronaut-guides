package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.Repository;
import io.micronaut.starter.build.RequiresRepository;

import java.util.Collections;
import java.util.List;

public interface RequiresJitpack extends RequiresRepository {
    @NonNull
    default List<Repository> getRepositories() {
        return Collections.singletonList(new Repository() {
            @Override
            public @NonNull String getId() {
                return "jitpack";
            }

            @Override
            public @NonNull String getUrl() {
                return "https://jitpack.io";
            }
        });
    }
}
