package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.AsciidocConfigurationProperties;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class CommonMacroSubstitutionTest {

    private static final GuidesOption PYRONAUT_OPTION = new GuidesOption(BuildTool.PYRONAUT, Language.PYTHON, TestFramework.JUNIT);
    private static final GuidesOption JAVA_OPTION = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);

    @Inject
    CommonMacroSubstitution commonMacroSubstitution;

    @TempDir
    Path commonsDir;

    @Test
    void testSubstitute() {
        String line = "common:header-top.adoc[]";
        String result = commonMacroSubstitution.substitute(line, null, null);
        String exptected = "include::{commonsDir}/common-header-top.adoc[]";
        assertEquals(exptected, result);
    }

    @Test
    void pyronautOptionIncludesPyronautVariantWhenItExists() throws IOException {
        Files.writeString(commonsDir.resolve("common-header-top-pyronaut.adoc"), "Pyronaut header");
        assertEquals("include::{commonsDir}/common-header-top-pyronaut.adoc[]",
                substitute("common:header-top.adoc[]", PYRONAUT_OPTION));
    }

    @Test
    void pyronautOptionKeepsIncludeWhenNoPyronautVariantExists() {
        assertEquals("include::{commonsDir}/common-header-top.adoc[]",
                substitute("common:header-top.adoc[]", PYRONAUT_OPTION));
    }

    @Test
    void nonPyronautOptionKeepsIncludeEvenWhenPyronautVariantExists() throws IOException {
        Files.writeString(commonsDir.resolve("common-header-top-pyronaut.adoc"), "Pyronaut header");
        assertEquals("include::{commonsDir}/common-header-top.adoc[]",
                substitute("common:header-top.adoc[]", JAVA_OPTION));
    }

    @Test
    void explicitPyronautNameIsNotResolvedAgain() throws IOException {
        Files.writeString(commonsDir.resolve("common-header-top-pyronaut.adoc"), "Pyronaut header");
        assertEquals("include::{commonsDir}/common-header-top-pyronaut.adoc[]",
                substitute("common:header-top-pyronaut.adoc[]", PYRONAUT_OPTION));
        assertEquals("include::{commonsDir}/common-header-top-pyronaut.adoc[]",
                substitute("common:header-top-pyronaut.adoc[]", JAVA_OPTION));
    }

    private String substitute(String line, GuidesOption option) {
        AsciidocConfigurationProperties configuration = new AsciidocConfigurationProperties();
        configuration.setCommonsDir(commonsDir.toString());
        return new CommonMacroSubstitution(configuration).substitute(line, null, option);
    }
}
