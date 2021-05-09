package io.micronaut.guides.feature;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.Writable;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Singleton
public class GoogleAppEngineGradle implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "google-app-engine-gradle";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("com.google.cloud.tools.appengine")
                    .lookupArtifactId("appengine-gradle-plugin").extension(new Writable() {
                        @Override
                        public void write(OutputStream outputStream) throws IOException {
                            outputStream.write(String.join("\n", Arrays.asList("appengine {",
                            "    stage.artifact = \"${buildDir}/libs/${project.name}-${project.version}-all.jar\"",
                            "    deploy {",
                            "        projectId = \"changethistoyourprojectid\"",
                            "    }",
                            "}")).getBytes(StandardCharsets.UTF_8));
                        }
                    })
                .build());
        }
    }
}
