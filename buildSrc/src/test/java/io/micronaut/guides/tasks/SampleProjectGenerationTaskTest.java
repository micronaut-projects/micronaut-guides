package io.micronaut.guides.tasks;

import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.App;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SampleProjectGenerationTaskTest {

    @Test
    void outputDirectoriesAreLimitedToTheRequestedLanguage() {
        Guide metadata = new Guide(null, null, null, null, null, null, null, null,
                false, false, null, List.of(Language.JAVA, Language.PYTHON), null,
                List.of(BuildTool.GRADLE, BuildTool.MAVEN), TestFramework.JUNIT,
                null, "multi-language", true, null, null,
                List.of(new App("default", null, null, null, null, null, null, null, null, null, null, null, false)));

        assertEquals(List.of("multi-language-pyronaut-python"),
                SampleProjectGenerationTask.outputDirectoryNames(metadata, Language.PYTHON));
        assertEquals(List.of("multi-language-gradle-java", "multi-language-maven-java",
                        "multi-language-pyronaut-python"),
                SampleProjectGenerationTask.outputDirectoryNames(metadata, null));
    }
}
