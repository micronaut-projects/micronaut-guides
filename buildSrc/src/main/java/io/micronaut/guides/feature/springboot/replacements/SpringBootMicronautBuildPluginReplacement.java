package io.micronaut.guides.feature.springboot.replacements;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.guides.feature.springboot.SpringBootApplicationFeature;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.MicronautBuildPlugin;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Replaces(MicronautBuildPlugin.class)
@Singleton
public class SpringBootMicronautBuildPluginReplacement extends MicronautBuildPlugin {

    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            if (!SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
                applyMicronautGradlePlugins(generatorContext);
            }
        }
    }
    private void applyMicronautGradlePlugins(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(shouldApplyMicronautApplicationGradlePlugin(generatorContext) ?
                micronautGradleApplicationPluginBuilder(generatorContext).build() :
                micronautLibraryGradlePlugin(generatorContext));
    }

    private static boolean shouldApplyMicronautApplicationGradlePlugin(GeneratorContext generatorContext) {
        return generatorContext.getFeatures().mainClass().isPresent() ||
                generatorContext.getFeatures().contains("oracle-function") ||
                generatorContext.getApplicationType() == ApplicationType.DEFAULT && generatorContext.getFeatures().contains("aws-lambda");
    }
}
