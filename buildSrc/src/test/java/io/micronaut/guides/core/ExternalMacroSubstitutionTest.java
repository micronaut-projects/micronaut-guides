package io.micronaut.guides.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class ExternalMacroSubstitutionTest {

    @Inject
    ExternalMacroSubstitution externalMacroSubstitution;

    @Test
    void testSubstitute() {
        String line = "external:micronaut-k8s/requirements.adoc[]";
        String result = externalMacroSubstitution.substitute(line, null, null);
        String exptected = "include::{guidesDir}/micronaut-k8s/requirements.adoc[]";
        assertEquals(exptected, result);
    }
}
