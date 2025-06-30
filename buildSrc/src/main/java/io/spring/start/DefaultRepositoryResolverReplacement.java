package io.spring.start;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.DefaultRepositoryResolver;
import io.micronaut.starter.build.MicronautSnapshotRepository;
import io.micronaut.starter.build.Repository;
import io.micronaut.starter.build.RepositoryResolver;
import jakarta.inject.Singleton;

import java.util.List;

@Replaces(RepositoryResolver.class)
@Singleton
public class DefaultRepositoryResolverReplacement extends DefaultRepositoryResolver {

    @Override
    public @NonNull List<Repository> resolveRepositories(@NonNull GeneratorContext generatorContext) {
        List<Repository> result = super.resolveRepositories(generatorContext);
        if (result.stream().noneMatch(r -> r instanceof MicronautSnapshotRepository)) {
            result.add(new MicronautSnapshotRepository());
        }
        return result;
    }
}
