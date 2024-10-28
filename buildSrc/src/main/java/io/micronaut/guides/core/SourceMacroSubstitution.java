package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.*;
import jakarta.inject.Singleton;

@Singleton
public class SourceMacroSubstitution extends SourceBlockMacroSubstitution{
    private static final String MACRO_SOURCE = "source";

    public SourceMacroSubstitution(GuidesConfiguration guidesConfiguration, LicenseLoader licenseLoader) {
        super(licenseLoader, guidesConfiguration);
    }

    @Override
    public String getMacroName() {
        return MACRO_SOURCE;
    }

    @Override
    public Classpath getClasspath() {
        return Classpath.MAIN;
    }

    @Override
    public FileType getFileType() {
        return FileType.CODE;
    }
}
