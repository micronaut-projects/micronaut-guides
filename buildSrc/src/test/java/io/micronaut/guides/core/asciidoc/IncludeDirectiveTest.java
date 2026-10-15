package io.micronaut.guides.core.asciidoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IncludeDirectiveTest {

    @Test
    void testJustTarget() {
        assertEquals("include::chapter01.adoc[]",
                IncludeDirective.builder().target("chapter01.adoc").build().toString());
    }

    @Test
    void testLevelOffset() {
        assertEquals("include::chapter01.adoc[leveloffset=+1]",
                IncludeDirective.builder().target("chapter01.adoc").levelOffset(1).build().toString());
    }

    @Test
    void testLines() {
        assertEquals("include::filename.txt[lines=5..10]",
                IncludeDirective.builder().target("filename.txt").lines(new Range(5,10)).build().toString());
    }

    @Test
    void testEncoding() {
        assertEquals("include::filename.txt[encoding=utf-8]",
                IncludeDirective.builder().target("filename.txt").encoding("utf-8").build().toString());
    }

    @Test
    void testTag() {
        assertEquals("include::core.rb[tag=parse]",
                IncludeDirective.builder().target("core.rb").tag("parse").build().toString());
    }

    @Test
    void testMultipleTags() {
        assertEquals("include::core.rb[tags=timings;parse]",
                IncludeDirective.builder().target("core.rb").tag("timings").tag("parse").build().toString());
    }

    @Test
    void testIndent() {
        assertEquals("include::core.rb[tags=timings;parse,indent=4]",
                IncludeDirective.builder().target("core.rb").tag("timings").tag("parse").indent(4).build().toString());
    }

    @Test
    void testOpt() {
        assertEquals("include::core.rb[tags=timings;parse,indent=4,opts=autoplay]",
                IncludeDirective.builder().target("core.rb").tag("timings").tag("parse").indent(4).opts("autoplay").build().toString());
    }
}