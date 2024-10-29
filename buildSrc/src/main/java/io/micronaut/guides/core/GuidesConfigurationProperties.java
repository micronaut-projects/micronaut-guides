package io.micronaut.guides.core;

import io.micronaut.context.annotation.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(GuidesConfigurationProperties.PREFIX)
public class GuidesConfigurationProperties implements GuidesConfiguration {
    public static final String PREFIX = "guides";
    public static final String GUIDES_URL = "https://guides.micronaut.io/latest/";
    private String title = "Micronaut Guides";
    private String homePageUrl = GUIDES_URL;
    private static final String DEFAULT_LICENSEHEADER = "classpath:LICENSEHEADER";
    private static final String DEFAULT_PACKAGE_NAME = "example.micronaut";
    private String licensePath = DEFAULT_LICENSEHEADER;
    private String packageName = DEFAULT_PACKAGE_NAME;
    private List<String> sourceFilesExtensions = List.of("java", "kotlin", "groovy");
    private static final String DEFAULT_APP_NAME = "default";
    private static final String HOMEPAGE_URL = "https://micronaut.io";
    private static final String LAUNCHER_URL = HOMEPAGE_URL + "/launch";

    @Override
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getLicensePath() {
        return licensePath;
    }

    public void setLicensePath(String licensePath) {
        this.licensePath = licensePath;
    }

    @Override
    public String getHomePageUrl() {
        return homePageUrl;
    }

    public void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDefaultAppName() {
        return DEFAULT_APP_NAME;
    }

    @Override
    public String getProjectGeneratorUrl() { return LAUNCHER_URL; }

    @Override
    public List<String> getFilesWithHeader() {
        return sourceFilesExtensions;
    }

    public void setFilesWithHeader(List<String> sourceFilesExtensions) {
        this.sourceFilesExtensions = sourceFilesExtensions;
    }
}
