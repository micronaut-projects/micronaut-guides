package io.micronaut.guides.core.asciidoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RangeTest {

    @Test
    void valid() {
        assertTrue(new Range(1, 2).isValid());
        assertFalse(new Range(2, 1).isValid());
        assertFalse(new Range(2, 2).isValid());
        assertTrue(new Range(16, -1).isValid());
    }
}