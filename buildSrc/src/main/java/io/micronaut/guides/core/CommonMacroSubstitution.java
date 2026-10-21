package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.AsciidocConfiguration;
import jakarta.inject.Singleton;

import java.nio.file.Path;

@Singleton
public class CommonMacroSubstitution extends LineMacroSubstitution {

    public CommonMacroSubstitution(AsciidocConfiguration asciidocConfiguration) {
        super(Path.of(asciidocConfiguration.getBaseDir()).resolve(asciidocConfiguration.getCommonsDir()));
    }

    @Override
    protected String getMacroName() {
        return "common";
    }

    @Override
    protected String getBaseDirectory() {
        return "{commonsDir}";
    }

    @Override
    protected String getPrefix() {
        return "common-";
    }
}
