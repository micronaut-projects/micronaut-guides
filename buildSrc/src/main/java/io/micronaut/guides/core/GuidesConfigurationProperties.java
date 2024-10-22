package io.micronaut.guides.core;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties(GuidesConfigurationProperties.PREFIX)
public class GuidesConfigurationProperties implements GuidesConfiguration {
    public static final String PREFIX = "guides";
    public static final String GUIDES_URL = "https://guides.micronaut.io/latest/";
    private String title = "Micronaut Guides";
    private String homePageUrl = GUIDES_URL;

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
}
