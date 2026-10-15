package io.micronaut.guides.core.asciidoc;

public enum Classpath {
    MAIN,
    TEST;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
