package io.micronaut.guides.core.asciidoc;

import io.micronaut.guides.core.*;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class AsciidocConverterTest {

    @Inject
    AsciidocConverter asciidocConverter;

    @Inject
    GuideParser guideParser;

    @Inject
    GuideProjectGenerator guideProjectGenerator;

    @Inject
    FilesTransferUtility filesTransferUtility;

    @Test
    void testConvert() throws IOException {
        String outputPath = "build/tmp/test/adding-commit-info";
        File outputDirectory = new File(outputPath);
        outputDirectory.mkdir();

        String path = "src/test/resources/other-guides/adding-commit-info";
        File file = new File(path);
        Guide guide = guideParser.parseGuideMetadata(file, "metadata.json").orElseThrow();

        guideProjectGenerator.generate(outputDirectory, guide);

        filesTransferUtility.transferFiles(file, outputDirectory, guide);

        File sourceFile = new File("src/test/resources/adding-commit-info-gradle-java.adoc");
        File destinationFile = new File("build/tmp/test/docs/adding-commit-info-gradle-java.html");
        asciidocConverter.convert(sourceFile, destinationFile);
        String expected = TestUtils.readFile(new File("src/test/resources/adding-commit-info-gradle-java-expected.html"));
        String result = TestUtils.readFile(destinationFile);
        assertEquals(expected, result);
    }

    @Test
    void testConvertIncludeAdoc() {
        File sourceFile = new File("src/test/resources/other-guides/adding-commit-info/adding-commit-info.adoc");
        File destinationFile = new File("build/tmp/test/docs/adding-commit-info.html");
        asciidocConverter.convert(sourceFile, destinationFile);
    }
}
