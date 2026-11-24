package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.AsciidocConfiguration;
import jakarta.inject.Singleton;

import java.nio.file.Path;

@Singleton
public class CalloutMacroSubstitution extends LineMacroSubstitution {

    public CalloutMacroSubstitution(AsciidocConfiguration asciidocConfiguration) {
        super(Path.of(asciidocConfiguration.getBaseDir()).resolve(asciidocConfiguration.getCalloutsDir()));
    }

    @Override
    protected String getMacroName() {
        return "callout";
    }

    @Override
    protected String getBaseDirectory() {
        return "{calloutsDir}";
    }

    @Override
    protected String getPrefix() {
        return "callout-";
    }
}
