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
public class GuidesFeedTest {
    @Inject
    JsonMapper jsonMapper;

    @Inject
    JsonSchemaProvider jsonSchemaProvider;

    private List<Guide> guides;

    @BeforeEach
    public void setup() throws Exception {
        File file = new File("src/test/resources/guides");
        this.guides = GuideUtils.parseGuidesMetadata(file,"metadata.json", jsonSchemaProvider.getSchema(), jsonMapper)
                .stream().filter( it -> it.publish()).toList();
    }

    @Test
    public void testJsonFeed() throws IOException, JSONException {
        String feed = GuidesFeed.jsonFeed(guides);
        String expected = """
                {
                  "version" : "https://jsonfeed.org/version/1.1",
                  "title" : "Micronaut Guides",
                  "home_page_url" : "https://guides.micronaut.io/latest/",
                  "feed_url" : "https://guides.micronaut.io/latest/feed.json",
                  "items" : [ {
                    "id" : "child",
                    "url" : "https://guides.micronaut.io/latest/child",
                    "title" : "Connect a Micronaut Data JDBC Application to Azure Database for MySQL",
                    "content_text" : "Learn how to connect a Micronaut Data JDBC application to a Microsoft Azure Database for MySQL",
                    "date_published" : "2022-02-17T00:00:00Z",
                    "authors" : [ {
                      "name" : "Graeme Rocher",
                      "empty" : false
                    } ],
                    "tags" : [ "cloud", "database", "micronaut-data", "jdbc", "flyway", "mysql", "Azure" ],
                    "language" : "LANG_ENGLISH"
                  }, {
                    "id" : "test",
                    "url" : "https://guides.micronaut.io/latest/test",
                    "title" : "1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API",
                    "content_text" : "This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.",
                    "date_published" : "2024-04-24T00:00:00Z",
                    "authors" : [ {
                      "name" : "Sergio del Amo",
                      "empty" : false
                    } ],
                    "tags" : [ "spring-boot" ],
                    "language" : "LANG_ENGLISH"
                  } ]
                }
                """;
        JSONAssert.assertEquals(expected, feed, JSONCompareMode.LENIENT);
    }
}
