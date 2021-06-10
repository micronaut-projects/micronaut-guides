package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.IOUtils;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

@Singleton
public class GitProperties implements Feature {

    private final PomDependencyVersionResolver resolver;
    private final ResourceLoader loader;

    public GitProperties(PomDependencyVersionResolver resolver,
                         ResourceLoader loader) {
        this.resolver = resolver;
        this.loader = loader;
    }

    @Override
    @NonNull
    public String getName() {
        return "git-properties";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("com.gorylenko.gradle-git-properties")
                    .lookupArtifactId("gradle-git-properties")
                    .build());
            return;
        }

        String version = resolver.resolve("git-commit-id-plugin")
                .orElseThrow(() -> new RuntimeException("git-commit-id-plugin not found in pom.xml"))
                .getVersion();
        String pluginXml = loadPluginXml().replace("@VERSION@", version);

        generatorContext.addBuildPlugin(MavenPlugin.builder()
                .artifactId("git-commit-id-plugin")
                .extension(out -> {
                    try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(out))) {
                        writer.println(pluginXml);
                    }
                })
                .build());
    }

    private String loadPluginXml() {
        InputStream is = loader.getResourceAsStream("git-commit-id-plugin.xml")
                .orElseThrow(() -> new RuntimeException("resource git-commit-id-plugin.xml not found"));
        try {
            return IOUtils.readText(new BufferedReader(new InputStreamReader(is)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }
}
