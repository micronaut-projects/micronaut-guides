import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import io.micronaut.guides.core.GuideMetadata;
import io.micronaut.guides.core.GuideAsciidocGenerator;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.JdkVersion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuideAsciidocGeneratorTest {

    @TempDir
    Path tempDir;

    private GuideMetadata metadata;
    private File inputDir;
    private File asciidocDir;
    private File projectDir;

    @BeforeEach
    void setUp() throws IOException {
        metadata = new GuideMetadata();
        metadata.setSlug("test-guide");
        metadata.setTitle("Test Guide");
        metadata.setIntro("This is a test guide");
        metadata.setAuthors(new HashSet<>(Arrays.asList("John Doe", "Jane Smith")));
        metadata.setAsciidoctor("guide.adoc");

        inputDir = tempDir.resolve("input").toFile();
        asciidocDir = tempDir.resolve("asciidoc").toFile();
        projectDir = tempDir.resolve("project").toFile();

        inputDir.mkdirs();
        asciidocDir.mkdirs();
        projectDir.mkdirs();

        // Create a sample guide.adoc file
        File guideFile = new File(inputDir, "guide.adoc");
        Files.write(guideFile.toPath(), Arrays.asList(
                "= Test Guide",
                "",
                "This is a test guide.",
                "",
                "include::common:license.adoc[]"
        ));

        // Create a sample version.txt file
        File versionFile = new File(projectDir, "version.txt");
        Files.write(versionFile.toPath(), "1.0.0".getBytes());
    }

    @Test
    void testGenerate() throws IOException {
        GuideAsciidocGenerator.generate(metadata, inputDir, asciidocDir, projectDir);

        // Check if the output file is created
        File outputFile = new File(asciidocDir, "test-guide-gradle-java.adoc");
        assertTrue(outputFile.exists());

        // Read the content of the generated file
        List<String> lines = Files.readAllLines(outputFile.toPath());

        // Verify the content
        assertTrue(lines.contains("= Test Guide"));
        assertTrue(lines.contains("This is a test guide."));
        assertTrue(lines.contains("include::common:license.adoc[]"));
        assertTrue(lines.stream().anyMatch(line -> line.contains("@micronaut@")));
        assertTrue(lines.stream().anyMatch(line -> line.contains("@authors@")));
    }

    @Test
    void testGenerateWithMaximumJavaVersion() {
        metadata.setMaximumJavaVersion(8);
        JdkVersion currentJdk = JdkVersion.valueOf("JDK_17");

        GuideAsciidocGenerator.generate(metadata, inputDir, asciidocDir, projectDir);

        if (currentJdk.majorVersion() > 8) {
            // Check that no file is generated when current JDK is higher than maximum
            File outputFile = new File(asciidocDir, "test-guide-gradle-java.adoc");
            assertFalse(outputFile.exists());
        } else {
            // Check that file is generated when current JDK is not higher than maximum
            File outputFile = new File(asciidocDir, "test-guide-gradle-java.adoc");
            assertTrue(outputFile.exists());
        }
    }

    @Test
    void testMainPath() {
        String result = GuideAsciidocGenerator.mainPath("myapp", "MyClass");
        assertEquals("myapp/src/main/@lang@/example/micronaut/MyClass.@languageextension@", result);

        result = GuideAsciidocGenerator.mainPath("", "MyClass");
        assertEquals("src/main/@lang@/example/micronaut/MyClass.@languageextension@", result);
    }

    @Test
    void testTestPath() {
        String result = GuideAsciidocGenerator.testPath("myapp", "MyClassTest", TestFramework.JUNIT);
        assertEquals("myapp/src/test/@lang@/example/micronaut/MyClassTest.@languageextension@", result);

        result = GuideAsciidocGenerator.testPath("myapp", "MyClassTest", TestFramework.SPOCK);
        assertEquals("myapp/src/test/@lang@/example/micronaut/MyClassSpec.@languageextension@", result);

        result = GuideAsciidocGenerator.testPath("", "MyClassTest", TestFramework.JUNIT);
        assertEquals("src/test/@lang@/example/micronaut/MyClassTest.@languageextension@", result);
    }

//    @Test
//    void testResolveAsciidoctorLanguage() {
//        assertEquals("yaml", GuideAsciidocGenerator.resolveAsciidoctorLanguage("config.yml"));
//        assertEquals("yaml", GuideAsciidocGenerator.resolveAsciidoctorLanguage("config.yaml"));
//        assertEquals("html", GuideAsciidocGenerator.resolveAsciidoctorLanguage("index.html"));
//        assertEquals("html", GuideAsciidocGenerator.resolveAsciidoctorLanguage("template.vm"));
//        assertEquals("html", GuideAsciidocGenerator.resolveAsciidoctorLanguage("template.hbs"));
//        assertEquals("xml", GuideAsciidocGenerator.resolveAsciidoctorLanguage("config.xml"));
//        assertEquals("java", GuideAsciidocGenerator.resolveAsciidoctorLanguage("MyClass.java"));
//    }
}