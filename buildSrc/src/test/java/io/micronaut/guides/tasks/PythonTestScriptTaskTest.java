package io.micronaut.guides.tasks;

import io.micronaut.guides.core.DefaultGuideParser;
import io.micronaut.guides.core.DefaultJsonSchemaProvider;
import io.micronaut.guides.core.Guide;
import io.micronaut.json.JsonMapper;
import org.gradle.api.Project;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.testfixtures.ProjectBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PythonTestScriptTaskTest {

    @Test
    void inheritedAppsChangeTheDeclaredInputsAndGeneratedScript(@TempDir Path directory) throws Exception {
        Path guides = Files.createDirectory(directory.resolve("guides"));
        Path base = Files.createDirectory(guides.resolve("a-base"));
        Path child = Files.createDirectory(guides.resolve("child"));
        Path metadataFile = child.resolve("metadata.json");
        JSONObject childMetadata = new JSONObject(Files.readString(
                Path.of("src/test/resources/guides-python/creating-your-first-micronaut-app/metadata.json")));
        childMetadata.put("base", "a-base");
        childMetadata.put("apps", new JSONArray());
        Files.writeString(metadataFile, childMetadata.toString());
        String unchangedChild = Files.readString(metadataFile);
        Path functions = Files.writeString(directory.resolve("functions.sh"), "# unchanged helper\n");
        Project project = ProjectBuilder.builder().withProjectDir(directory.toFile()).build();
        PythonTestScriptTask task = project.getTasks().create("pythonTestScript", PythonTestScriptTask.class);
        task.getGuideSlug().set("child");
        task.getMetadataFile().set(metadataFile.toFile());
        task.getPyronautTestFunctionsFile().set(functions.toFile());
        task.getScriptFile().set(directory.resolve("python-test.sh").toFile());
        DefaultGuideParser parser = new DefaultGuideParser(new DefaultJsonSchemaProvider(), JsonMapper.createDefault());
        Map<String, Object> originalInputs = null;
        String originalScript = null;
        for (String appName : new String[]{"first", "second"}) {
            Files.writeString(base.resolve("metadata.json"),
                    "{\"publish\":false,\"apps\":[{\"name\":\"" + appName + "\"}]}");
            Guide merged = parser.parseGuidesMetadata(guides.toFile(), "metadata.json").stream()
                    .filter(guide -> guide.slug().equals("child")).findFirst().orElseThrow();
            task.setMetadata(merged);
            Map<String, Object> inputs = Map.copyOf(task.getInputs().getProperties());
            task.perform();
            String script = Files.readString(task.getScriptFile().get().getAsFile().toPath());
            assertTrue(script.contains("cd " + appName + "\n"));
            if (originalInputs != null) {
                assertNotEquals(originalScript, script);
                assertNotEquals(originalInputs, inputs, "Changing inherited applications must invalidate the script inputs");
            }
            originalInputs = inputs;
            originalScript = script;
        }
        assertEquals(unchangedChild, Files.readString(metadataFile));
    }

    @Test
    void pyronautTestFunctionsResourceIsAnInput() {
        Method property = Arrays.stream(PythonTestScriptTask.class.getMethods())
                .filter(method -> method.getName().equals("getPyronautTestFunctionsFile"))
                .findFirst()
                .orElse(null);

        assertNotNull(property, "The helper resource must be a task property");
        assertTrue(property.isAnnotationPresent(InputFile.class));
        assertEquals(PathSensitivity.RELATIVE, property.getAnnotation(PathSensitive.class).value());
    }
}
