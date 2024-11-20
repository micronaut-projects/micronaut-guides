package io.micronaut.guides.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class GuideLinkMacroSubstitutionTest {

    @Inject
    GuideLinkMacroSubstitution guideLinkMacroSubstitution;

    @Test
    void testSubstitute() {
        String str = "Deploy the _auth.yml_ file that we created in the guideLink:micronaut-k8s[Kubernetes and the Micronaut Framework] guide guideLink:micronaut-k8s[Kubernetes and the Micronaut Framework] and guideLink:micronaut-cli-jwkgen[JWK generation with a Micronaut command line application].";
        String result = guideLinkMacroSubstitution.substitute(str, null, null);
        String expected = "Deploy the _auth.yml_ file that we created in the link:micronaut-k8s.html[Kubernetes and the Micronaut Framework] guide link:micronaut-k8s.html[Kubernetes and the Micronaut Framework] and link:micronaut-cli-jwkgen.html[JWK generation with a Micronaut command line application].";
        assertEquals(expected, result);
    }
}
