package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.rss.DefaultRssFeedRenderer;
import io.micronaut.rss.RssChannel;
import io.micronaut.rss.RssItem;
import io.micronaut.rss.language.RssLanguage;
import jakarta.inject.Singleton;

import java.io.StringWriter;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Singleton
public class DefaultRssFeedGenerator implements RssFeedGenerator {
    private final GuidesConfiguration guidesConfiguration;

    public DefaultRssFeedGenerator(GuidesConfiguration guidesConfiguration) {
        this.guidesConfiguration = guidesConfiguration;
    }

    @NonNull
    public String rssFeed(@NonNull List<Guide> metadatas) {
        RssChannel.Builder rssBuilder = rssBuilder();
        for (Guide metadata : metadatas) {
            rssBuilder.item(rssFeedElement(metadata));
        }
        DefaultRssFeedRenderer rssFeedRenderer = new DefaultRssFeedRenderer();
        StringWriter writer = new StringWriter();
        rssFeedRenderer.render(writer, rssBuilder.build());
        return writer.toString();
    }

    private RssChannel.Builder rssBuilder() {
        return RssChannel.builder(
                        guidesConfiguration.getTitle(),
                        guidesConfiguration.getHomePageUrl(),
                        "RSS feed for " + guidesConfiguration.getTitle())
                .language(RssLanguage.LANG_ENGLISH);
    }

    private RssItem rssFeedElement(Guide metadata) {
        RssItem.Builder rssItemBuilder = RssItem.builder()
                .guid(metadata.slug())
                .title(metadata.title())
                .description(metadata.intro())
                .pubDate(ZonedDateTime.of(metadata.publicationDate(), LocalTime.of(0, 0), ZoneOffset.UTC))
                .link(guidesConfiguration.getHomePageUrl() + metadata.slug());
        for (String author : metadata.authors()) {
            rssItemBuilder.author(author);
        }
        rssItemBuilder.category(GuideUtils.getTags(metadata));
        return rssItemBuilder.build();
    }
}
