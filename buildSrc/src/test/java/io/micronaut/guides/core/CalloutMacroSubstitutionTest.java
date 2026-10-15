package io.micronaut.guides.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class CalloutMacroSubstitutionTest {

    @Inject
    CalloutMacroSubstitution calloutMacro;

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
}
