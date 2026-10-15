package io.micronaut.guides.core;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties(GuidesConfigurationProperties.PREFIX + ".json-feed")
public class JsonFeedConfigurationProperties implements JsonFeedConfiguration {
    private static final String JSON_FEED_FILENAME = "feed.json";
    private String feedUrl = GuidesConfigurationProperties.GUIDES_URL + JSON_FEED_FILENAME;
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
}
