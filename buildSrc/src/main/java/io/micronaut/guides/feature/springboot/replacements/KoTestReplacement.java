package io.micronaut.guides.feature.springboot.replacements;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.guides.feature.springboot.SpringBootApplicationFeature;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.test.KoTest;
import jakarta.inject.Singleton;

@Replaces(KoTest.class)
@Singleton
public class KoTestReplacement extends KoTest {

    @Override
    public void doApply(GeneratorContext generatorContext) {
        if (!SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
            super.doApply(generatorContext);
        }
    }
}
