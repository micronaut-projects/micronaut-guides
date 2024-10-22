package io.micronaut.guides.core;

import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class RssFeedGenerationTest {
    @Inject
    JsonMapper jsonMapper;

    @Inject
    JsonSchemaProvider jsonSchemaProvider;

    @Inject
    RssFeedGenerator rssFeedGenerator;

    private List<Guide> guides;

    @BeforeEach
    public void setup() throws Exception {
        File file = new File("src/test/resources/guides");
        this.guides = GuideUtils.parseGuidesMetadata(file,"metadata.json", jsonSchemaProvider.getSchema(), jsonMapper)
                .stream().filter(Guide::publish).toList();
    }

    @Test
    void testRssFeed() {
        String feed = rssFeedGenerator.rssFeed(guides);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rss xmlns:content=\"http://purl.org/rss/1.0/modules/content/\" version=\"2.0\"><channel><title>Micronaut Guides</title><link>https://guides.micronaut.io/latest/</link><description>RSS feed for Micronaut Guides</description><language>en</language><item><title>Connect a Micronaut Data JDBC Application to Azure Database for MySQL</title><link>https://guides.micronaut.io/latest/child</link><description>Learn how to connect a Micronaut Data JDBC application to a Microsoft Azure Database for MySQL</description><author>Graeme Rocher</author><category>cloud</category><category>database</category><category>Azure</category><category>flyway</category><category>jdbc</category><category>mysql</category><category>micronaut-data</category><category>data-jdbc</category><guid>child</guid><pubDate>Thu, 17 Feb 2022 00:00:00 Z</pubDate></item><item><title>1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API</title><link>https://guides.micronaut.io/latest/test</link><description>This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.</description><author>Sergio del Amo</author><category>spring-boot-starter-web</category><category>jackson-databind</category><category>spring-boot</category><category>assertj</category><category>boot-to-micronaut-building-a-rest-api</category><category>json-path</category><guid>test</guid><pubDate>Wed, 24 Apr 2024 00:00:00 Z</pubDate></item></channel></rss>";
        assertEquals(expected, feed);
    }
}
