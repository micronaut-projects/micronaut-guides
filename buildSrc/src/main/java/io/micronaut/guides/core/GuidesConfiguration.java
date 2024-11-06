package io.micronaut.guides.core;

import io.micronaut.starter.options.JdkVersion;

import java.util.List;

public interface GuidesConfiguration {
    String getHomePageUrl();

    String getTitle();

    String getLicensePath();

    String getPackageName();

    String getDefaultAppName();

    String getProjectGeneratorUrl();

    int getDefaultMinJdk();

    String getApiUrl();

    String getVersionPath();

    String getEnvJdkVersion();

    List<String> getFilesWithHeader();

    JdkVersion getDefaultJdkVersion();

    List<JdkVersion> getJdkVersionsSupportedByGraalvm();
}
