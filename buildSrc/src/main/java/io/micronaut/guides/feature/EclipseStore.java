package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class EclipseStore implements Feature {
    public static final String NAME = "eclipsestore";
    private static final String MICRONAUT_ECLIPSESTORE_VERSION = "1.0.1";
    private static final String PROPERTY_MICRONAUT_ECLIPSESTORE_VERSION = "micronaut.eclipsestore.version";
    private static final String ARTIFACT_ID_MICRONAUT_ECLIPSESTORE_ANNOTATIONS = "micronaut-eclipsestore-annotations";
    private static final String ARTIFACT_ID_MICRONAUT_ECLIPSESTORE = "micronaut-eclipsestore";

    private static final String GROUP_ID_MICRONAUT_ECLIPSESTORE = "io.micronaut.eclipsestore";
    private static final Dependency DEPENDENCY_ECLIPSESTORE = Dependency.builder()
            .groupId(GROUP_ID_MICRONAUT_ECLIPSESTORE)
            .artifactId(ARTIFACT_ID_MICRONAUT_ECLIPSESTORE)
            .compile()
            .version(MICRONAUT_ECLIPSESTORE_VERSION)
            .build();

    private static final Dependency DEPENDENCY_ECLIPSESTORE_ANNOTATION_PROCESSORS = Dependency.builder()
            .groupId(GROUP_ID_MICRONAUT_ECLIPSESTORE)
            .artifactId(ARTIFACT_ID_MICRONAUT_ECLIPSESTORE_ANNOTATIONS)
            .compile()
            .version(MICRONAUT_ECLIPSESTORE_VERSION)
            .build();

    @Override
    public @NonNull String getName() {
        return NAME;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.annotationProcessor(generatorContext.getBuildTool(),
                GROUP_ID_MICRONAUT_ECLIPSESTORE,
                ARTIFACT_ID_MICRONAUT_ECLIPSESTORE_ANNOTATIONS,
                PROPERTY_MICRONAUT_ECLIPSESTORE_VERSION
        ).version(MICRONAUT_ECLIPSESTORE_VERSION));
        generatorContext.addDependency(DEPENDENCY_ECLIPSESTORE_ANNOTATION_PROCESSORS);
        generatorContext.addDependency(DEPENDENCY_ECLIPSESTORE);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
