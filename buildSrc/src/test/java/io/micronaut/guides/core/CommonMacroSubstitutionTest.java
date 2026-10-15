package io.micronaut.guides.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class CommonMacroSubstitutionTest {

    @Inject
    CommonMacroSubstitution commonMacroSubstitution;

    @Test
    void testSubstitute() {
        String line = "common:header-top.adoc[]";
        String result = commonMacroSubstitution.substitute(line, null, null);
        String exptected = "include::{commonsDir}/common-header-top.adoc[]";
        assertEquals(exptected, result);
    }
}
