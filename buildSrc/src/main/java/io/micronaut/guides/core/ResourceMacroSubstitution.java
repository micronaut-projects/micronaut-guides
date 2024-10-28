package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.Classpath;
import jakarta.inject.Singleton;

@Singleton
public class ResourceMacroSubstitution extends SourceBlockMacroSubstitution{
    private static final String MACRO_RESOURCE = "resource";
    public ResourceMacroSubstitution(GuidesConfiguration guidesConfiguration, LicenseLoader licenseLoader) {
        super(licenseLoader, guidesConfiguration);
    }

    @Override
    public String getMacroName() {
        return MACRO_RESOURCE;
    }

    @Override
    public Classpath getClasspath() {
        return Classpath.MAIN;
    }

    @Override
    public FileType getFileType() {
        return FileType.RESOURCE;
    }
}
