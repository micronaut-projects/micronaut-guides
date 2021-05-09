package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.TestFramework;
import javax.inject.Singleton;

@Singleton
public class GebFeature implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "geb";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getTestFramework().equals(TestFramework.JUNIT)) {
            generatorContext.addDependency(Dependency.builder().groupId("org.gebish").lookupArtifactId("geb-junit").test().build());
        } else if (generatorContext.getTestFramework().equals(TestFramework.SPOCK)) {
            generatorContext.addDependency(Dependency.builder().groupId("org.gebish").lookupArtifactId("geb-spock").test().build());
        }
        generatorContext.addDependency(Dependency.builder().groupId("org.seleniumhq.selenium").lookupArtifactId("htmlunit-driver").test().build());
    }
}
