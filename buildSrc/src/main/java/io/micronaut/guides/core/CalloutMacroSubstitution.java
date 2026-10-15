package io.micronaut.guides.core;

import jakarta.inject.Singleton;

@Singleton
public class CalloutMacroSubstitution extends LineMacroSubstitution {
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
