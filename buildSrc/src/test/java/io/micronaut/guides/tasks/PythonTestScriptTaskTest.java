package io.micronaut.guides.tasks;

import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PythonTestScriptTaskTest {

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
