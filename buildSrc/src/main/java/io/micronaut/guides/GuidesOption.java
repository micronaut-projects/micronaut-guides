package io.micronaut.guides;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;

@Introspected
public class GuidesOption {

    @NonNull
    private BuildTool buildTool;

    @NonNull
    private Language language;

    @NonNull
    private TestFramework testFramework;

    public GuidesOption(@NonNull BuildTool buildTool,
                        @NonNull Language language,
                        @NonNull TestFramework testFramework) {
        this.buildTool = buildTool;
        this.language = language;
        this.testFramework = testFramework;
    }

    @NonNull
    public BuildTool getBuildTool() {
        return buildTool;
    }

    public void setBuildTool(@NonNull BuildTool buildTool) {
        this.buildTool = buildTool;
    }

    @NonNull
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(@NonNull Language language) {
        this.language = language;
    }

    @NonNull
    public TestFramework getTestFramework() {
        return testFramework;
    }

    public void setTestFramework(@NonNull TestFramework testFramework) {
        this.testFramework = testFramework;
    }
}
