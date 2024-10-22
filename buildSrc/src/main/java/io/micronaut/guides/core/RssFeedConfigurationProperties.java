package io.micronaut.guides.core;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties(GuidesConfigurationProperties.PREFIX + ".rss-feed")
public class RssFeedConfigurationProperties implements RssFeedConfiguration {
    private static final String JSON_FEED_FILENAME = "rss.xml";
    private String filename = JSON_FEED_FILENAME;

    @Override
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
