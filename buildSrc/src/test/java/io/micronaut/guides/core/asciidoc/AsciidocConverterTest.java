package io.micronaut.guides.core.asciidoc;

import io.micronaut.guides.core.FilesTransferUtility;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuideParser;
import io.micronaut.guides.core.GuideProjectGenerator;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

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

        String path = "src/test/resources/guides/adding-commit-info";
        File file = new File(path);
        Guide guide = guideParser.parseGuideMetadata(file, "metadata.json").orElseThrow();

        guideProjectGenerator.generate(outputDirectory, guide);

        filesTransferUtility.transferFiles(file, outputDirectory, guide);

        File adocFile = new File("src/test/resources/adding-commit-info-gradle-java.adoc");
        String result = asciidocConverter.convert(adocFile);
    }
}
