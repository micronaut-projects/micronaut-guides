package io.micronaut.guides.core;

import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class PythonTestRequirementsTest {
    @Inject
    GuideParser guideParser;

    @Inject
    TestScriptGenerator testScriptGenerator;

    @TempDir
    Path temporaryDirectory;

    @Test
    void installsTestRequirementsWithTheProjectPythonBeforeTesting() throws Exception {
        for (var generator : scripts().entrySet()) {
            ScriptResult result = run(generator, true, "bin/python", 0);
            assertEquals(0, result.exitCode(), result.output());
            assertEquals(List.of("pyronaut install", "python -m pip install -r requirements-test.txt",
                    "pyronaut validate-config", "pyronaut test"), result.commands());
        }
    }

    @Test
    void supportsTheWindowsVirtualEnvironmentLayout() throws Exception {
        for (var generator : scripts().entrySet()) {
            ScriptResult result = run(generator, true, "Scripts/python.exe", 0);
            assertEquals(0, result.exitCode(), result.output());
            assertEquals(List.of("pyronaut install", "python -m pip install -r requirements-test.txt",
                    "pyronaut validate-config", "pyronaut test"), result.commands());
        }
    }

    @Test
    void leavesGuidesWithoutTestRequirementsUnchanged() throws Exception {
        for (var generator : scripts().entrySet()) {
            ScriptResult result = run(generator, false, "bin/python", 0);
            assertEquals(0, result.exitCode(), result.output());
            assertEquals(List.of("pyronaut install", "pyronaut validate-config", "pyronaut test"), result.commands());
        }
    }

    @Test
    void propagatesTestDependencyInstallationFailures() throws Exception {
        for (var generator : scripts().entrySet()) {
            ScriptResult result = run(generator, true, "bin/python", 42);
            assertNotEquals(0, result.exitCode(), result.output());
            assertTrue(result.commands().contains("python -m pip install -r requirements-test.txt"));
        }
    }

    @Test
    void doesNotFallBackToAGlobalPythonWhenTheProjectEnvironmentIsMissing() throws Exception {
        for (var generator : scripts().entrySet()) {
            ScriptResult result = run(generator, true, null, 0);
            assertNotEquals(0, result.exitCode(), result.output());
        }
    }

    private Map<String, String> scripts() {
        Guide guide = guideParser.parseGuideMetadata(
                new File("src/test/resources/file-transfer/python-resources"), "metadata.json").orElseThrow();
        Map<String, String> scripts = new LinkedHashMap<>();
        scripts.put("core", testScriptGenerator.generateTestScript(new ArrayList<>(List.of(guide))));
        // The Gradle generator intentionally omits Python projects when CI is set.
        if (System.getenv("CI") == null) {
            scripts.put("gradle", io.micronaut.guides.TestScriptGenerator.generateScript(
                    new ArrayList<>(List.of(guide)), false, false, Language.PYTHON));
        }
        return scripts;
    }

    private ScriptResult run(Map.Entry<String, String> generator, boolean testRequirements,
                             String pythonPath, int pipExitCode) throws Exception {
        Path workingDirectory = Files.createDirectory(temporaryDirectory.resolve(generator.getKey() + " with spaces"));
        Path project = Files.createDirectory(workingDirectory.resolve("python-resources-pyronaut-python"));
        Path commands = workingDirectory.resolve("commands.log");
        Path executables = Files.createDirectory(workingDirectory.resolve("commands"));
        writeExecutable(executables.resolve("pyronaut"), """
                #!/usr/bin/env bash
                printf 'pyronaut %s\\n' "$*" >> "$COMMAND_LOG"
                """);
        if (testRequirements) {
            Files.writeString(project.resolve("requirements-test.txt"), "pypdf\n");
        }
        if (pythonPath != null) {
            Path python = project.resolve(".venv").resolve(pythonPath);
            Files.createDirectories(python.getParent());
            writeExecutable(python, """
                    #!/usr/bin/env bash
                    printf 'python %s\\n' "$*" >> "$COMMAND_LOG"
                    exit "$PIP_EXIT_CODE"
                    """);
        }
        Path script = workingDirectory.resolve("test.sh");
        Files.writeString(script, generator.getValue());
        ProcessBuilder builder = new ProcessBuilder("bash", script.toString())
                .directory(workingDirectory.toFile()).redirectErrorStream(true);
        builder.environment().put("PATH", executables + File.pathSeparator + System.getenv("PATH"));
        builder.environment().put("COMMAND_LOG", commands.toString());
        builder.environment().put("PIP_EXIT_CODE", Integer.toString(pipExitCode));
        Process process = builder.start();
        try {
            assertTrue(process.waitFor(15, TimeUnit.SECONDS), "Generated test script did not finish");
            String output = new String(process.getInputStream().readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            return new ScriptResult(process.exitValue(), Files.readAllLines(commands), output);
        } finally {
            process.destroyForcibly();
        }
    }

    private void writeExecutable(Path file, String content) throws IOException {
        Files.writeString(file, content);
        file.toFile().setExecutable(true);
    }

    private record ScriptResult(int exitCode, List<String> commands, String output) {
    }
}
