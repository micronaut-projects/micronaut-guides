package io.micronaut.guides.feature;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.LanguageSpecificFeature;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;

@Singleton
public class MultitenancyGorm implements LanguageSpecificFeature {

    @NonNull
    @Override
    public String getName() {
        return "multitenancy-gorm";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

     @Override
    public Language getRequiredLanguage() {
        return Language.GROOVY;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.groovy")
                .lookupArtifactId("micronaut-multitenancy-gorm")
                .compile()
                .build());
    }
}
