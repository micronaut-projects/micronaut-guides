package io.micronaut.guides;

import io.micronaut.guides.core.App;
import io.micronaut.guides.core.Guide;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndexGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void pyronautReadLinkPointsToPublishedGuidePage() throws IOException {
        File template = tempDir.resolve("template.html").toFile();
        Files.writeString(template.toPath(), String.join("\n",
                "<html>",
                "<head><title>@title@</title>@twittercard@</head>",
                "<body class='@bodyclass@'>",
                "<div id=\"breadcrumbs\">@breadcrumb@</div>",
                "<main id=\"main\">@content@</main>",
                "<div>@toccontent@</div>",
                "</body>",
                "</html>"));

        File distDir = tempDir.resolve("dist").toFile();
        assertTrue(distDir.mkdirs());

        Guide guide = new Guide(
                "Python Guide",
                "A guide with a Pyronaut variant.",
                List.of("Micronaut"),
                List.of("Core Basics"),
                LocalDate.of(2026, 5, 18),
                null,
                null,
                null,
                false,
                false,
                null,
                List.of(Language.JAVA, Language.PYTHON),
                List.of(),
                List.of(BuildTool.GRADLE, BuildTool.PYRONAUT),
                TestFramework.JUNIT,
                List.of(),
                "python-guide",
                true,
                null,
                Map.of(),
                List.of(new App("default", null, null, null, List.of(), null, null, null, null, null, null, null, true)),
                true,
                false);

        IndexGenerator.generateGuidesIndex(template, distDir, List.of(guide), null);

        String html = Files.readString(distDir.toPath().resolve("python-guide.html"));
        assertTrue(html.contains("<a href='python-guide-pyronaut-python.html'>Read</a>"));
        assertFalse(html.contains("<a href='python-guide.html'>Read</a>"));
        assertFalse(html.contains("python-guide-gradle-python.html"));
        assertFalse(html.contains("python-guide-maven-python.html"));
        assertTrue(html.contains("<td colspan='3'></td><td><a href='python-guide-pyronaut-python.html'>Read</a></td>"));
    }

    @Test
    void jvmOnlyGuideDoesNotRenderPythonColumn() throws IOException {
        File template = tempDir.resolve("jvm-template.html").toFile();
        Files.writeString(template.toPath(), String.join("\n",
                "<html>",
                "<head><title>@title@</title>@twittercard@</head>",
                "<body class='@bodyclass@'>",
                "<div id=\"breadcrumbs\">@breadcrumb@</div>",
                "<main id=\"main\">@content@</main>",
                "<div>@toccontent@</div>",
                "</body>",
                "</html>"));

        File distDir = tempDir.resolve("jvm-dist").toFile();
        assertTrue(distDir.mkdirs());
        Guide guide = new Guide(
                "JVM Guide",
                "A JVM guide.",
                List.of("Micronaut"),
                List.of("Core Basics"),
                LocalDate.of(2026, 5, 18),
                null,
                null,
                null,
                false,
                false,
                null,
                List.of(Language.JAVA),
                List.of(),
                List.of(BuildTool.GRADLE),
                TestFramework.JUNIT,
                List.of(),
                "jvm-guide",
                true,
                null,
                Map.of(),
                List.of(new App("default", null, null, null, List.of(), null, null, null, null, null, null, null, true)),
                false,
                false);

        IndexGenerator.generateGuidesIndex(template, distDir, List.of(guide), null);

        String html = Files.readString(distDir.toPath().resolve("jvm-guide.html"));
        assertFalse(html.contains("python.svg"));
        assertFalse(html.contains("Pyronaut"));
    }
}
