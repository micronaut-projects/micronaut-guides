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

@MicronautTest(startApplication = false)
public class JsonFeedGeneratorTest {
    @Inject
    JsonMapper jsonMapper;

    @Inject
    JsonSchemaProvider jsonSchemaProvider;

    @Inject
    JsonFeedGenerator jsonFeedGenerator;

    private List<Guide> guides;

    @BeforeEach
    public void setup() {
        File file = new File("src/test/resources/guides");
        GuideParser guideParser = new DefaultGuideParser(jsonSchemaProvider, jsonMapper);
        this.guides = guideParser.parseGuidesMetadata(file, "metadata.json")
                .stream().filter(Guide::publish).toList();
    }

    @Test
    public void testJsonFeed() throws IOException, JSONException {
        String feed = jsonFeedGenerator.jsonFeedString(guides);
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
                    "tags" : [ "cloud", "database", "Azure", "flyway", "jdbc", "mysql", "micronaut-data", "data-jdbc" ],
                    "language" : "LANG_ENGLISH"
                  }, {
                    "id" : "creating-your-first-micronaut-app",
                    "url" : "https://guides.micronaut.io/latest/creating-your-first-micronaut-app",
                    "title" : "Creating your first Micronaut application",
                    "content_text" : "Learn how to create a Hello World Micronaut application with a controller and a functional test.",
                    "date_published" : "2018-05-23T00:00:00Z",
                    "authors" : [ {
                      "name" : "Iván López",
                      "empty" : false
                    }, {
                      "name" : "Sergio del Amo",
                      "empty" : false
                    } ],
                    "tags" : [ "junit", "getting-started", "graalvm" ],
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
                    "tags" : [ "spring-boot-starter-web", "jackson-databind", "spring-boot", "assertj", "boot-to-micronaut-building-a-rest-api", "json-path" ],
                    "language" : "LANG_ENGLISH"
                  } ]
                }
                """;
        JSONAssert.assertEquals(expected, feed, JSONCompareMode.LENIENT);
    }
}
