package io.micronaut.guides.core;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.json.JsonMapper;
import io.micronaut.rss.DefaultRssFeedRenderer;
import io.micronaut.rss.RssChannel;
import io.micronaut.rss.RssItem;
import io.micronaut.rss.jsonfeed.JsonFeed;
import io.micronaut.rss.jsonfeed.JsonFeedAuthor;
import io.micronaut.rss.jsonfeed.JsonFeedItem;
import io.micronaut.rss.language.RssLanguage;
import org.reactivestreams.Publisher;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class GuidesFeed {
    private static final String GUIDES_URL = "https://guides.micronaut.io";
    private static final String JSON_FEED_FILENAME = "feed.json";
    public static String jsonFeed(List<Guide> metadatas) throws IOException {
        JsonFeed.Builder jsonFeedBuilder = JsonFeed.builder()
                .version("https://jsonfeed.org/version/1.1")
                .title("Micronaut Guides")
                .homePageUrl(GUIDES_URL + "/latest/")
                .feedUrl(GUIDES_URL + "/latest/" + JSON_FEED_FILENAME);
        for (Guide metadata : metadatas) {
            jsonFeedBuilder.item(jsonFeedItem(metadata));
        }
        JsonFeed jsonFeed = jsonFeedBuilder.build();
        JsonMapper jsonMapper = JsonMapper.createDefault();
        return jsonMapper.writeValueAsString(jsonFeed);
    }

    static JsonFeedItem jsonFeedItem(Guide metadata) {
        JsonFeedItem.Builder jsonFeedItemBuilder = JsonFeedItem.builder()
                .id(metadata.slug())
                .title(metadata.title())
                .contentText(metadata.intro())
                .language(RssLanguage.LANG_ENGLISH)
                .datePublished(ZonedDateTime.of(metadata.publicationDate(), LocalTime.of(0, 0), ZoneOffset.UTC))
                .url(GUIDES_URL + "/latest/" + metadata.slug());
        for (String author: metadata.authors()) {
            jsonFeedItemBuilder.author(JsonFeedAuthor.builder().name(author).build());
        }
        for (String t : GuideUtils.getTags(metadata)) {
            jsonFeedItemBuilder.tag(t);
        }
        return jsonFeedItemBuilder.build();
    }

    public static String rssFeed(List<Guide> metadatas){
         RssChannel.Builder rssBuilder = RssChannel.builder(
                "Micronaut Guides", GUIDES_URL + "/latest/", "RSS feed for Micronaut Guides")
                 .language(RssLanguage.LANG_ENGLISH);
        for (Guide metadata : metadatas) {
            rssBuilder.item(rssFeedElement(metadata));
        }
        DefaultRssFeedRenderer rssFeedRenderer = new DefaultRssFeedRenderer();
        StringWriter writer = new StringWriter();
        rssFeedRenderer.render(writer, rssBuilder.build());
        return writer.toString();
    }

    static RssItem rssFeedElement(Guide metadata){
        RssItem.Builder rssItemBuilder = RssItem.builder()
                        .guid(metadata.slug())
                        .title(metadata.title())
                        .description(metadata.intro())
                        .pubDate(ZonedDateTime.of(metadata.publicationDate(), LocalTime.of(0, 0), ZoneOffset.UTC))
                        .link(GUIDES_URL + "/latest/" + metadata.slug());
        for (String author: metadata.authors()) {
            rssItemBuilder.author(author);
        }
        rssItemBuilder.category(GuideUtils.getTags(metadata));
        return rssItemBuilder.build();
    }
}
