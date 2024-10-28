package io.micronaut.guides.core.asciidoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SourceBlockTest {

    @Test
    void sourceBlockRendering() {
        assertEquals("""
                [source,ruby]
                ----
                include::app.rb[]
                ----""", SourceBlock.builder()
                        .language("ruby")
                        .includeDirective(IncludeDirective.builder().target("app.rb").build())
                        .build().toString());

        assertEquals("""
                [source,ruby]
                .Gemfile.lock               
                ----
                include::app.rb[]
                ----""", SourceBlock.builder()
                .title("Gemfile.lock")
                .language("ruby")
                .includeDirective(IncludeDirective.builder().target("app.rb").build())
                .build().toString());
    }
}