package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class ControlPanelUi extends AbstractFeature {

    public ControlPanelUi() {
        super("control-panel-ui", "micronaut-control-panel-ui", Scope.COMPILE);
    }

    @Override
    protected void addDependency(GeneratorContext generatorContext,
                                 String artifactId,
                                 Scope scope) {
        addDependencyWithoutLookup(generatorContext, "io.micronaut.controlpanel");
    }
}
