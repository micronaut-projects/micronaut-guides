package io.micronaut.guides.core;

import jakarta.inject.Singleton;

@Singleton
public class RawTestMacroSubstitution extends TestMacroSubstitution {
    private static final String MACRO_RAW_TEST = "rawTest";

    public RawTestMacroSubstitution(GuidesConfiguration guidesConfiguration, LicenseLoader licenseLoader) {
        super(guidesConfiguration, licenseLoader);
    }

    @Override
    public String getMacroName() {
        return MACRO_RAW_TEST;
    }
}
