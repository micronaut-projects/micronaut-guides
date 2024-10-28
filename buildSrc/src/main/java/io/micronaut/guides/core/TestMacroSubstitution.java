package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.guides.core.asciidoc.Classpath;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import static io.micronaut.starter.api.TestFramework.SPOCK;

@Singleton
public class TestMacroSubstitution  extends SourceBlockMacroSubstitution {
    private static final String MACRO_TEST = "test";
    private static final String SUFFIX_TEST = "Test";
    public static final String SUFFIX_SPEC = "Spec";
    public TestMacroSubstitution(GuidesConfiguration guidesConfiguration, LicenseLoader licenseLoader) {
        super(licenseLoader, guidesConfiguration);
    }

    @Override
    public String getMacroName() {
        return MACRO_TEST;
    }

    @Override
    public Classpath getClasspath() {
        return Classpath.TEST;
    }

    @Override
    public FileType getFileType() {
        return FileType.CODE;
    }

    @Override
    public String condensedTarget(@NotNull AsciidocMacro asciidocMacro, GuidesOption option) {
        String target = asciidocMacro.target();
        if (target.endsWith(SUFFIX_TEST)) {
            target = target.substring(0, target.indexOf(SUFFIX_TEST));
            target += option.getTestFramework() == SPOCK ? SUFFIX_SPEC : SUFFIX_TEST;
        }
        return target;
    }
}
