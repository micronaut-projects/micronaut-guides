package io.micronaut.guides;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class IndexGeneratorTest {

    @Test
    void pythonJsonContainsFlatPythonGuideEntries() throws Exception {
        String json = IndexGenerator.generatePythonJsonIndex(
                new File("src/test/resources/guides-python"),
                "metadata.json"
        );

        JSONObject guide = new JSONArray(json).getJSONObject(0);

        assertEquals("Creating your first Pyronaut application", guide.getString("title"));
        assertEquals("Learn how to create a Hello World Pyronaut application with a controller and a functional test.", guide.getString("intro"));
        assertEquals("https://micronaut-projects.github.io/micronaut-guides/latest/creating-your-first-micronaut-app-pyronaut-python.html", guide.getString("url"));
        assertFalse(guide.has("options"));
        assertEquals("Getting Started", guide.getJSONArray("categories").getString(0));
    }
}
