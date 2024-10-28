package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.Classpath;
import jakarta.inject.Singleton;

@Singleton
public class ZipIncludeMacroSubstitution extends SourceBlockMacroSubstitution {
    private static final String MACRO_ZIPINCLUDE = "zipInclude";
    public ZipIncludeMacroSubstitution(GuidesConfiguration guidesConfiguration, LicenseLoader licenseLoader) {
        super(licenseLoader, guidesConfiguration);
    }

    @Override
    public String getMacroName() { return MACRO_ZIPINCLUDE; }

    @Override
    protected Classpath getClasspath() { return Classpath.MAIN; }

    @Override
    protected FileType getFileType() { return FileType.RESOURCE; }

    @Override
    protected String sourceTitle(
            String appName,
            String condensedTarget,
            Classpath classpath,
            String language,
            String packageName) {
        return condensedTarget;
    }
}
