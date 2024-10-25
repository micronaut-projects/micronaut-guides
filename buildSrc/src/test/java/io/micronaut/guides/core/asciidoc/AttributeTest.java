package io.micronaut.guides.core.asciidoc;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttributeTest {

    @Test
    void testAttributeParsing() {
        assertEquals(List.of(
                new Attribute("tag", List.of("main")),
                new Attribute("indent", List.of("0"))
        ), Attribute.of("tag=main,indent=0"));
    }

}