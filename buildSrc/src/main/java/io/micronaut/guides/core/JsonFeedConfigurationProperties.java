package io.micronaut.guides.core;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("guides.json-feed")
public class JsonFeedConfigurationProperties implements JsonFeedConfiguration {
    private static final String GUIDES_URL = "https://guides.micronaut.io/latest/";
    private static final String JSON_FEED_FILENAME = "feed.json";
    private String title = "Micronaut Guides";
    private String feedUrl = GUIDES_URL + JSON_FEED_FILENAME;
    private String homePageUrl = GUIDES_URL;
    private String filename = JSON_FEED_FILENAME;

    @Override
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
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
}
