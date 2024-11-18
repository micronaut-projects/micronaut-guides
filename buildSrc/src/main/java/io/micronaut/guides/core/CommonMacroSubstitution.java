package io.micronaut.guides.core;

import jakarta.inject.Singleton;

@Singleton
public class CommonMacroSubstitution extends LineMacroSubstitution {

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
