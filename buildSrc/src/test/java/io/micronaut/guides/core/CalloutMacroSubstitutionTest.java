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
public class CalloutMacroSubstitutionTest {

    private static final GuidesOption PYRONAUT_OPTION = new GuidesOption(BuildTool.PYRONAUT, Language.PYTHON, TestFramework.JUNIT);
    private static final GuidesOption JAVA_OPTION = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);

    @Inject
    CalloutMacroSubstitution calloutMacro;

    @TempDir
    Path calloutsDir;

    @Test
    void testSubstitute() {
        String line = "callout:get[arg0=index,arg1=/hello]";
        String result = calloutMacro.substitute(line, null, null);
        String exptected = """
                :arg0: index
                :arg1: /hello
                include::{calloutsDir}/callout-get[]""";
        assertEquals(exptected, result);
    }

    @Test
    void pyronautOptionIncludesPyronautVariantWhenItExists() throws IOException {
        Files.writeString(calloutsDir.resolve("callout-micronaut-test-pyronaut.adoc"), "<.> pytest fixture");
        assertEquals("include::{calloutsDir}/callout-micronaut-test-pyronaut.adoc[]",
                substitute("callout:micronaut-test[]", PYRONAUT_OPTION));
        assertEquals("""
                :arg0: index
                include::{calloutsDir}/callout-micronaut-test-pyronaut.adoc[]""",
                substitute("callout:micronaut-test.adoc[arg0=index]", PYRONAUT_OPTION));
    }

    @Test
    void pyronautOptionKeepsIncludeWhenNoPyronautVariantExists() {
        assertEquals("include::{calloutsDir}/callout-micronaut-test[]",
                substitute("callout:micronaut-test[]", PYRONAUT_OPTION));
    }

    @Test
    void nonPyronautOptionKeepsIncludeEvenWhenPyronautVariantExists() throws IOException {
        Files.writeString(calloutsDir.resolve("callout-micronaut-test-pyronaut.adoc"), "<.> pytest fixture");
        assertEquals("include::{calloutsDir}/callout-micronaut-test[]",
                substitute("callout:micronaut-test[]", JAVA_OPTION));
    }

    @Test
    void explicitPyronautNameIsNotResolvedAgain() throws IOException {
        Files.writeString(calloutsDir.resolve("callout-micronaut-test-pyronaut.adoc"), "<.> pytest fixture");
        assertEquals("include::{calloutsDir}/callout-micronaut-test-pyronaut[]",
                substitute("callout:micronaut-test-pyronaut[]", PYRONAUT_OPTION));
        assertEquals("include::{calloutsDir}/callout-micronaut-test-pyronaut[]",
                substitute("callout:micronaut-test-pyronaut[]", JAVA_OPTION));
    }

    private String substitute(String line, GuidesOption option) {
        AsciidocConfigurationProperties configuration = new AsciidocConfigurationProperties();
        configuration.setCalloutsDir(calloutsDir.toString());
        return new CalloutMacroSubstitution(configuration).substitute(line, null, option);
    }
}
