package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.json.JsonMapper;
import io.micronaut.rss.jsonfeed.JsonFeed;
import io.micronaut.rss.jsonfeed.JsonFeedAuthor;
import io.micronaut.rss.jsonfeed.JsonFeedItem;
import io.micronaut.rss.language.RssLanguage;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Singleton
public class DefaultJsonFeedGenerator implements JsonFeedGenerator {
    private final GuidesConfiguration guidesConfiguration;
    private final JsonFeedConfiguration jsonFeedConfiguration;
    private final JsonMapper jsonMapper;

    public DefaultJsonFeedGenerator(GuidesConfiguration guidesConfiguration,
                                    JsonFeedConfiguration jsonFeedConfiguration,
                                    JsonMapper jsonMapper) {
        this.guidesConfiguration = guidesConfiguration;
        this.jsonFeedConfiguration = jsonFeedConfiguration;
        this.jsonMapper = jsonMapper;
    }

    public JsonFeed jsonFeed(List<Guide> metadatas) {
        JsonFeed.Builder jsonFeedBuilder = jsonFeedBuilder();
        for (Guide metadata : metadatas) {
            jsonFeedBuilder.item(jsonFeedItem(metadata));
        }
        return jsonFeedBuilder.build();
    }

    @Override
    @NonNull
    public String jsonFeedString(@NonNull List<Guide> metadatas) throws IOException {
        JsonFeed jsonFeed = jsonFeed(metadatas);
        return jsonMapper.writeValueAsString(jsonFeed);
    }

    private JsonFeed.Builder jsonFeedBuilder() {
        return JsonFeed.builder()
                .version(JsonFeed.VERSION_JSON_FEED_1_1)
                .title(guidesConfiguration.getTitle())
                .homePageUrl(guidesConfiguration.getHomePageUrl())
                .feedUrl(jsonFeedConfiguration.getFeedUrl());
    }

    private JsonFeedItem jsonFeedItem(Guide metadata) {
        JsonFeedItem.Builder jsonFeedItemBuilder = JsonFeedItem.builder()
                .id(metadata.slug())
                .title(metadata.title())
                .contentText(metadata.intro())
                .language(RssLanguage.LANG_ENGLISH)
                .datePublished(ZonedDateTime.of(metadata.publicationDate(), LocalTime.of(0, 0), ZoneOffset.UTC))
                .url(guidesConfiguration.getHomePageUrl() + metadata.slug());
        for (String author : metadata.authors()) {
            jsonFeedItemBuilder.author(JsonFeedAuthor.builder().name(author).build());
        }
        for (String t : GuideUtils.getTags(metadata)) {
            jsonFeedItemBuilder.tag(t);
        }
        return jsonFeedItemBuilder.build();
    }
}
