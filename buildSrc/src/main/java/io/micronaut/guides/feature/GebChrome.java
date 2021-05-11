package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.gradle.GradlePlugin;

import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;

@Singleton
public class GebChrome extends Geb {

    @NonNull
    @Override
    public String getName() {
        return "geb-chrome";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);

        generatorContext.addDependency(Dependency.builder().groupId("org.seleniumhq.selenium").lookupArtifactId("selenium-chrome-driver").test().build());

        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("com.github.erdi.webdriver-binaries")
                    .lookupArtifactId("webdriver-binaries-gradle-plugin")
                    .extension(outputStream -> outputStream.write(gradleChromeTemplate().getBytes(StandardCharsets.UTF_8)))
                    .build());
        }
    }

    private String gradleChromeTemplate() {
        return "webdriverBinaries {\n" +
                "    chromedriver {\n" +
                "        // Get latest for your OS and Chrome from\n" +
                "        // https://github.com/webdriverextensions/webdriverextensions-maven-plugin-repository/blob/master/repository-3.0.json\n" +
                "        version = '88.0.4324.96'\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "tasks.withType(Test) {\n" +
                "    systemProperty \"geb.env\", System.getProperty('geb.env')\n" +
                "    systemProperty \"download.folder\", System.getProperty('download.folder')\n" +
                "}\n";
    }
}
