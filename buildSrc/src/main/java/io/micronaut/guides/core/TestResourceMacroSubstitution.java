package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.Classpath;
import jakarta.inject.Singleton;

@Singleton
public class TestResourceMacroSubstitution extends SourceBlockMacroSubstitution {
    private static final String MACRO_TESTRESOURCE = "testResource";
    public TestResourceMacroSubstitution(GuidesConfiguration guidesConfiguration, LicenseLoader licenseLoader) {
        super(licenseLoader, guidesConfiguration);
    }

    @Override
    public String getMacroName() {
        return MACRO_TESTRESOURCE;
    }

    @Override
    public Classpath getClasspath() {
        return Classpath.TEST;
    }

    @Override
    public FileType getFileType() {
        return FileType.RESOURCE;
    }
}
