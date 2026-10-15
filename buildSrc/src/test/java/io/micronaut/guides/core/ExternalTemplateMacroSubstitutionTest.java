package io.micronaut.guides.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class ExternalTemplateMacroSubstitutionTest {
    @Inject
    private ExternalTemplateMacroSubstitution externalTemplateMacroSubstitution;

    @Test
    public void testSubstitute() {
        String line = "external-template:micronaut-k8s-oci/k8s-microservice.adoc[arg0=users]";
        String result = externalTemplateMacroSubstitution.substitute(line, null, null);
        String exptected = """
                :arg0: users
                include::{guidesDir}/micronaut-k8s-oci/k8s-microservice.adoc[]""";
        assertEquals(exptected, result);
    }
}
