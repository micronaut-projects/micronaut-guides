package io.micronaut.guides.core;

import jakarta.inject.Singleton;

@Singleton
public class ExternalMacroSubstitution extends LineMacroSubstitution {

    @Override
    protected String getMacroName() {
        return "external";
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
