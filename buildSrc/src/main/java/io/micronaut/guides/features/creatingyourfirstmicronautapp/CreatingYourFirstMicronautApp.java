package io.micronaut.guides.features.creatingyourfirstmicronautapp;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.guides.features.GuideFeature;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.guides.features.creatingyourfirstmicronautapp.templates.*;
import javax.inject.Singleton;

@Singleton
class CreatingYourFirstMicronautApp extends GuideFeature {

    @NonNull
    @Override
    public String getName() {
        return "guide-creating-your-first-micronaut-app";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Project project = generatorContext.getProject();
        helloController(generatorContext, project);
    }

    private void helloController(GeneratorContext generatorContext, Project project) {
        String helloController = generatorContext.getSourcePath("/{packagePath}/HelloController");
        generatorContext.addTemplate("hellocontroller", helloController,
                hellocontrollerJava.template(project),
                hellocontrollerKotlin.template(project),
                hellocontrollerGroovy.template(project));

        String helloControllerTest = generatorContext.getTestSourcePath("/{packagePath}/HelloController");

        generatorContext.addTemplate("hellocontrollertest", helloControllerTest,
                hellocontrollertestJava.template(project),
                hellocontrollertestKotlin.template(project),
                hellocontrollertestGroovy.template(project));
    }

}

