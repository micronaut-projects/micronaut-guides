package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

@Singleton
public class DockerPushGoogleCloudContainerRegistry extends DockerPushFeature {

    @Override
    public String getTitle() {
        return "Docker Push Google Cloud Container Registry";
    }

    @Override
    protected String getImage() {
        return "gcr.io/micronaut-guides/micronautguide:latest";
    }

    @NonNull
    @Override
    public String getName() {
        return "docker-push-gcr";
    }
}
