package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.Repository;

public class GraalVMStagingRepository implements Repository {
    @Override
    public @NonNull String getId() {
        return "graalvm-maven";
    }

    @Override
    public @NonNull String getUrl() {
        return "";
    }

    @Override
    public boolean isSnapshot() {
        return true;
    }
}
