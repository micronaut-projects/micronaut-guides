package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.Repository;
import io.micronaut.starter.build.RequiresRepository;
import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class MicronautGraalpy extends AbstractFeature implements RequiresRepository {

    public MicronautGraalpy() {
        super("micronaut-graalpy", Scope.COMPILE);
    }

    @Override
    public @NonNull List<Repository> getRepositories() {
        return List.of(new GraalVMStagingRepository());
    }
}
