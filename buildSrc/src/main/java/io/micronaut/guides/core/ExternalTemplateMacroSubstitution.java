package io.micronaut.guides.core;

import jakarta.inject.Singleton;

@Singleton
public class ExternalTemplateMacroSubstitution extends LineMacroSubstitution {
    @Override
    protected String getMacroName() {
        return "external-template";
    }

    @Override
    protected String getBaseDirectory() {
        return "{guidesDir}";
    }

    @Override
    protected String getPrefix() {
        return "";
    }
}
