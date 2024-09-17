package io.micronaut.guides.feature;

import io.micronaut.guides.feature.template.graalpyMavenPluginPackage;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@Singleton
class MicronautGraalpy extends AbstractFeature  {

    private static final String GROUP_ID_GRAALVM_PYTHON = "org.graalvm.python";
    private static final String ARTIFACT_ID_GRAALPY_MAVEN_PLUGIN = "graalpy-maven-plugin";

    private final CoordinateResolver coordinateResolver;

    MicronautGraalpy(CoordinateResolver coordinateResolver) {
        super("graalpy", "micronaut-graalpy");
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            BuildProperties props = generatorContext.getBuildProperties();
            coordinateResolver.resolve(ARTIFACT_ID_GRAALPY_MAVEN_PLUGIN)
                    .ifPresent(coordinate -> props.put("graalpy.maven.plugin.version", coordinate.getVersion()));
            generatorContext.addBuildPlugin(graalpyMavenPlugin());
        }
    }

    protected MavenPlugin graalpyMavenPlugin() {
        return MavenPlugin.builder()
                .groupId(GROUP_ID_GRAALVM_PYTHON)
                .artifactId(ARTIFACT_ID_GRAALPY_MAVEN_PLUGIN)
                .extension(new RockerWritable(graalpyMavenPluginPackage.template(pythonPackages())))
                .build();
    }

    protected List<String> pythonPackages() {
        return Collections.emptyList();
    }
}
