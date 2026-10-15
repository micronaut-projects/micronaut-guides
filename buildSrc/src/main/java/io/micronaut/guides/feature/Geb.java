package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.options.MicronautVersion;
import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.TEST;
import static io.micronaut.starter.options.TestFramework.JUNIT;
import static io.micronaut.starter.options.TestFramework.SPOCK;

@Singleton
public class Geb extends AbstractFeature {

    public Geb() {
        super("geb", "htmlunit-driver", TEST);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);

        if (generatorContext.getTestFramework().equals(JUNIT)) {
            addDependency(generatorContext, "geb-junit", TEST);
        } else if (generatorContext.getTestFramework().equals(SPOCK)) {


            generatorContext.addDependency(Dependency.builder()
                    .groupId("org.apache.groovy.geb")
                    .artifactId("geb-spock")
                            .version("8.0.1")
                    .scope(TEST)
                    .exclude(Dependency.builder()
                            .groupId("org.apache.groovy.geb")
                            .artifactId("geb-implicit-assertions")
                            .build()));
        }
    }
}
