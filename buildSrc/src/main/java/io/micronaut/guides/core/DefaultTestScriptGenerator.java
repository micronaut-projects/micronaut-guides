package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.micronaut.starter.options.BuildTool.GRADLE;
import static io.micronaut.starter.options.BuildTool.MAVEN;
import static io.micronaut.starter.options.BuildTool.PYRONAUT;


@Singleton
public class DefaultTestScriptGenerator implements TestScriptGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTestScriptGenerator.class);

    private final GuidesConfiguration guidesConfiguration;
    private final GuideParser guideParser;

    public DefaultTestScriptGenerator(GuidesConfiguration guidesConfiguration, GuideParser guideParser) {
        this.guidesConfiguration = guidesConfiguration;
        this.guideParser = guideParser;
    }

    private static List<String> guidesChanged(List<String> changedFiles) {
        return changedFiles.stream()
                .filter(path -> path.startsWith("guides"))
                .map(path -> {
                    String guideFolder = path.substring("guides/".length());
                    return guideFolder.substring(0, guideFolder.indexOf('/'));
                })
                .distinct()
                .collect(Collectors.toList());
    }

    private static boolean changesMicronautVersion(List<String> changedFiles) {
        return changedFiles.stream().anyMatch(file -> file.contains("version.txt"));
    }

    private static boolean changesDependencies(List<String> changedFiles, List<String> changedGuides) {
        if (!changedGuides.isEmpty()) {
            return false;
        }
        return changedFiles.stream().anyMatch(file -> file.contains("pom.xml"));
    }

    private static boolean changesBuildScr(List<String> changedFiles) {
        return changedFiles.stream().anyMatch(file -> file.contains("buildSrc"));
    }

    private static String scriptForFolder(String nestedFolder,
                                          String folder,
                                          boolean stopIfFailure,
                                          BuildTool buildTool,
                                          boolean noDaemon,
                                          boolean nativeTest,
                                          boolean validateLicense) {
        String testCopy = nativeTest ? "native tests" : "tests";
        StringBuilder bashScript = new StringBuilder(String.format(
                """
                        cd %s
                        echo "-------------------------------------------------"
                        echo "Executing '%s' %s"
                        """,
                nestedFolder, folder, testCopy
        ));

        if (noDaemon) {
            bashScript.append("kill_kotlin_daemon\n");
        }

        if (nativeTest) {
            bashScript.append(String.format(
                    "%s || EXIT_STATUS=$?\n",
                    buildTool == BuildTool.MAVEN ? "./mvnw -Pnative test" : "./gradlew nativeTest"
            ));
        } else if (buildTool == BuildTool.PYRONAUT) {
            bashScript.append("run_pyronaut_tests || EXIT_STATUS=$?\n");
        } else {
            String mavenCommand = validateLicense ? "./mvnw -q test spotless:check" : "./mvnw -q test";
            bashScript.append(String.format(
                    """
                            %s || EXIT_STATUS=$?
                            echo "Stopping shared test resources service (if created)"
                            %s > /dev/null 2>&1 || true
                            """,
                    buildTool == BuildTool.MAVEN ? mavenCommand : "./gradlew -q check",
                    buildTool == BuildTool.MAVEN ? "./mvnw -q mn:stop-testresources-service" : "./gradlew -q stopTestResourcesService"
            ));
        }

        if (noDaemon) {
            bashScript.append("kill_kotlin_daemon\n");
        }

        bashScript.append("cd ..\n");

        if (stopIfFailure) {
            bashScript.append(String.format(
                    """
                            if [ $EXIT_STATUS -ne 0 ]; then
                              echo "'%s' %s failed => exit $EXIT_STATUS"
                              exit $EXIT_STATUS
                            fi
                            """,
                    folder, testCopy
            ));
        } else {
            bashScript.append(String.format(
                    """
                            if [ $EXIT_STATUS -ne 0 ]; then
                              FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" %s)
                              echo "'%s' %s failed => exit $EXIT_STATUS"
                            fi
                            EXIT_STATUS=0
                            """,
                    folder, folder, testCopy
            ));
        }

        return bashScript.toString();
    }

    private static boolean shouldSkip(Guide metadata,
                                      List<String> guidesChanged,
                                      boolean forceExecuteEveryTest,
                                      GuidesConfiguration guidesConfiguration) {

        if (!GuideGenerationUtils.process(metadata, false, guidesConfiguration)) {
            return true;
        }

        if (forceExecuteEveryTest) {
            return false;
        }

        return !guidesChanged.contains(metadata.slug());
    }

    @Override
    public boolean supportsNativeTest(App app, GuidesOption guidesOption) {
        return isMicronautFramework(app) &&
                guidesOption.getBuildTool() == GRADLE &&
                supportsNativeTest(guidesOption.getLanguage()) &&
                guidesOption.getTestFramework() == TestFramework.JUNIT;
    }

    @Override
    public boolean isMicronautFramework(App app) {
        return app.framework() == null || app.framework().equals("Micronaut");
    }

    @Override
    public boolean supportsNativeTest(Language language) {
        return language != Language.GROOVY;
    }

    @Override
    public String generateNativeTestScript(@NonNull @NotNull List<Guide> metadatas) {
        return generateScript(metadatas, false, true);
    }

    @Override
    public String generateTestScript(@NonNull @NotNull List<Guide> metadatas) {
        return generateScript(metadatas, false, false);
    }

    @Override
    public String generatePythonTestScript(@NonNull @NotNull List<Guide> metadatas) {
        return generateScript(metadatas, false, false, true);
    }

    public String generateScript(File guidesFolder,
                                 String metadataConfigName,
                                 boolean stopIfFailure,
                                 List<String> changedFiles) {
        List<String> slugsChanged = guidesChanged(changedFiles);
        boolean forceExecuteEveryTest = changesMicronautVersion(changedFiles) ||
                changesDependencies(changedFiles, slugsChanged) ||
                changesBuildScr(changedFiles) ||
                (System.getenv(guidesConfiguration.getEnvGithubWorkflow()) != null &&
                        !System.getenv(guidesConfiguration.getEnvGithubWorkflow()).equals(guidesConfiguration.getGithubWorkflowJavaCi())) ||
                (changedFiles.isEmpty() && System.getenv(guidesConfiguration.getEnvGithubWorkflow()) == null);

        List<Guide> metadatas = guideParser.parseGuidesMetadata(guidesFolder, metadataConfigName);
        metadatas = metadatas.stream()
                .filter(metadata -> !shouldSkip(metadata, slugsChanged, forceExecuteEveryTest, guidesConfiguration))
                .collect(Collectors.toList());
        return generateScript(metadatas, stopIfFailure, false);
    }

    public String generateScript(List<Guide> metadatas,
                                 boolean stopIfFailure,
                                 boolean nativeTest) {
        return generateScript(metadatas, stopIfFailure, nativeTest, false);
    }

    public String generateScript(List<Guide> metadatas,
                                 boolean stopIfFailure,
                                 boolean nativeTest,
                                 boolean pythonTest) {
        StringBuilder bashScript = new StringBuilder("""
                #!/usr/bin/env bash
                set -e

                FAILED_PROJECTS=()
                EXIT_STATUS=0

                kill_kotlin_daemon () {
                  echo "Killing KotlinCompile daemon to pick up fresh properties (due to kapt and java > 17)"
                  for daemon in $(jps | grep KotlinCompile | cut -d' ' -f1); do
                    echo "Killing $daemon"
                    kill -9 $daemon
                  done
                }""");
        if (pythonTest) {
            bashScript.append("""

                    pyronaut_repo_python () {
                      if [ -n "${PYTHON:-}" ]; then
                        echo "$PYTHON"
                      elif command -v python3 > /dev/null 2>&1; then
                        echo "python3"
                      else
                        echo "python"
                      fi
                    }

                    set_pyronaut_local_repositories () {
                      if [ -z "${PYRONAUT_LOCAL_REPOSITORY:-}" ]; then
                        return 0
                      fi

                    "$(pyronaut_repo_python)" -c 'exec(__import__("sys").stdin.read())' "$PYRONAUT_LOCAL_REPOSITORY" <<'PY'
                    from pathlib import Path
                    import json
                    import re
                    import sys

                    repository = sys.argv[1]
                    pyproject = Path("pyproject.toml")
                    text = pyproject.read_text(encoding="utf-8")
                    section = "[tool.pyronaut]"
                    section_index = text.find(section)
                    if section_index < 0:
                        raise SystemExit("Missing [tool.pyronaut] in pyproject.toml")

                    next_section_index = text.find("\\n[", section_index + len(section))
                    if next_section_index < 0:
                        next_section_index = len(text)
                    section_text = text[section_index:next_section_index]
                    repositories = f'repositories = [{json.dumps(repository)}, "mavenCentral", "https://central.sonatype.com/repository/maven-snapshots/"]'
                    section_text, count = re.subn(r'(?ms)^repositories\\s*=\\s*\\[[^\\]]*\\]', repositories, section_text, count=1)
                    if count == 0:
                        section_text = section_text.replace(section + "\\n", section + "\\n" + repositories + "\\n", 1)
                    text = text[:section_index] + section_text + text[next_section_index:]
                    pyproject.write_text(text, encoding="utf-8")
                    PY
                    }

                    set_pyronaut_project_venv () {
                      if [ -e .venv ] || [ -L .venv ] || [ -z "${VIRTUAL_ENV:-}" ] || [ ! -x "$VIRTUAL_ENV/bin/python" ]; then
                        return 0
                      fi
                      ln -s "$VIRTUAL_ENV" .venv
                    }

                    set_pyronaut_test_resources () {
                      "$(pyronaut_repo_python)" -c 'exec(__import__("sys").stdin.read())' <<'PY'
                    from pathlib import Path
                    import re

                    pyproject = Path("pyproject.toml")
                    text = pyproject.read_text(encoding="utf-8")
                    section = "[tool.pyronaut.test-resources]"
                    section_index = text.find(section)
                    if section_index < 0:
                        raise SystemExit(f"Missing {section} in pyproject.toml")

                    next_section_index = text.find("\\n[", section_index + len(section))
                    if next_section_index < 0:
                        next_section_index = len(text)
                    section_text = text[section_index:next_section_index]
                    section_text, count = re.subn(r'(?m)^enabled\\s*=\\s*(?:true|false)', "enabled = true", section_text, count=1)
                    if count == 0:
                        section_text = section_text.replace(section + "\\n", section + "\\nenabled = true\\n", 1)
                    text = text[:section_index] + section_text + text[next_section_index:]
                    pyproject.write_text(text, encoding="utf-8")
                    PY
                    }

                    set_pyronaut_native_toolchain () {
                      if [ "${PYRONAUT_USE_NATIVE_TOOLCHAIN:-}" != "true" ]; then
                        return 0
                      fi

                      "$(pyronaut_repo_python)" -c 'exec(__import__("sys").stdin.read())' <<'PY'
                    from pathlib import Path

                    pyproject = Path("pyproject.toml")
                    text = pyproject.read_text(encoding="utf-8")
                    section = "[tool.pyronaut.toolchain]"
                    section_index = text.find(section)
                    if section_index < 0:
                        separator = "" if not text or text.endswith("\\n") else "\\n"
                        text += f'{separator}\\n{section}\\ntype = "native"\\n'
                    else:
                        next_section_index = text.find("\\n[", section_index + len(section))
                        if next_section_index < 0:
                            next_section_index = len(text)
                        section_text = text[section_index:next_section_index]
                        lines = section_text.splitlines(keepends=True)
                        for index, line in enumerate(lines):
                            if line.lstrip().startswith('type') and '=' in line:
                                line_ending = '\\n' if line.endswith('\\n') else ''
                                lines[index] = 'type = "native"' + line_ending
                                break
                        else:
                            lines.insert(1, 'type = "native"\\n')
                        section_text = "".join(lines)
                        text = text[:section_index] + section_text + text[next_section_index:]
                    pyproject.write_text(text, encoding="utf-8")
                    PY
                    }

                    set_pyronaut_local_versions () {
                      if [ -z "${PYRONAUT_LOCAL_CORE_VERSION:-}" ] && [ -z "${PYRONAUT_LOCAL_PLATFORM_VERSION:-}" ]; then
                        return 0
                      fi

                    "$(pyronaut_repo_python)" -c 'exec(__import__("sys").stdin.read())' "${PYRONAUT_LOCAL_CORE_VERSION:-}" "${PYRONAUT_LOCAL_PLATFORM_VERSION:-}" <<'PY'
                    from pathlib import Path
                    import re
                    import sys

                    core_version = sys.argv[1]
                    platform_version = sys.argv[2]
                    pyproject = Path("pyproject.toml")
                    text = pyproject.read_text(encoding="utf-8")

                    def set_section_version(source, section, version):
                        if not version:
                            return source
                        section_index = source.find(section)
                        if section_index < 0:
                            raise SystemExit(f"Missing {section} in pyproject.toml")

                        next_section_index = source.find("\\n[", section_index + len(section))
                        if next_section_index < 0:
                            next_section_index = len(source)
                        section_text = source[section_index:next_section_index]
                        replacement = f'version = "{version}"'
                        section_text, count = re.subn(r'(?m)^version\\s*=\\s*"[^"]*"', replacement, section_text, count=1)
                        if count == 0:
                            section_text = section_text.replace(section + "\\n", section + "\\n" + replacement + "\\n", 1)
                        return source[:section_index] + section_text + source[next_section_index:]

                    text = set_section_version(text, "[tool.pyronaut.core]", core_version)
                    text = set_section_version(text, "[tool.pyronaut.platform]", platform_version)
                    pyproject.write_text(text, encoding="utf-8")
                    PY
                    }

                    run_pyronaut_install () {
                      local install_args=()
                      if [ -n "${PYRONAUT_LOCAL_REPOSITORY:-}" ]; then
                        install_args+=(--local-repository "${PYRONAUT_DEPENDENCY_CACHE:-.pyronaut-m2}")
                      fi
                      if [ "${PYRONAUT_REFRESH_DEPENDENCIES:-}" = "true" ]; then
                        install_args+=(--refresh)
                      fi

                      if [ -n "${PYRONAUT_INSTALL_EXECUTABLE:-}" ]; then
                        "$PYRONAUT_INSTALL_EXECUTABLE" "${install_args[@]}"
                      else
                        pyronaut install "${install_args[@]}"
                      fi
                    }

                    run_pyronaut_validate_config () {
                      if [ -n "${PYRONAUT_VALIDATE_CONFIG_EXECUTABLE:-}" ]; then
                        "$PYRONAUT_VALIDATE_CONFIG_EXECUTABLE"
                      else
                        pyronaut validate-config
                      fi
                    }

                    run_pyronaut_process () {
                      if [ -n "${PYRONAUT_PROCESS_EXECUTABLE:-}" ]; then
                        "$PYRONAUT_PROCESS_EXECUTABLE"
                      else
                        pyronaut process
                      fi
                    }

                    run_pyronaut_cli () {
                      if [ -n "${PYRONAUT_CLI_PYTHONPATH:-}" ] && [ -d "$PYRONAUT_CLI_PYTHONPATH" ]; then
                        PYTHONPATH="${PYRONAUT_CLI_PYTHONPATH}${PYTHONPATH:+:$PYTHONPATH}" "$(pyronaut_repo_python)" -m pyronaut_cli_v2 "$@"
                      else
                        pyronaut "$@"
                      fi
                    }

                    run_pyronaut_test () {
                      local test_args=()
                      if [ -n "${PYRONAUT_LOCAL_REPOSITORY:-}" ]; then
                        test_args+=(--local-repository "${PYRONAUT_DEPENDENCY_CACHE:-.pyronaut-m2}")
                      fi
                      run_pyronaut_cli test "${test_args[@]}"
                    }

                    run_pyronaut_tests () {
                      if [ -z "${PYRONAUT_LOCAL_REPOSITORY:-}" ]; then
                        set_pyronaut_test_resources
                        set_pyronaut_native_toolchain
                        set_pyronaut_project_venv
                        run_pyronaut_install && run_pyronaut_validate_config && run_pyronaut_process && run_pyronaut_test
                        return $?
                      fi

                      local tmp_dir
                      tmp_dir="$(mktemp -d "${TMPDIR:-/tmp}/pyronaut-guide.XXXXXX")"
                      tar --exclude='./__pyronaut__' --exclude='./.pytest_cache' --exclude='./build' --exclude='./dist' --exclude='./*.egg-info' -cf - . | (cd "$tmp_dir" && tar -xf -)
                      (
                        cd "$tmp_dir"
                        set_pyronaut_local_repositories
                        set_pyronaut_local_versions
                        set_pyronaut_test_resources
                        set_pyronaut_native_toolchain
                        set_pyronaut_project_venv
                        run_pyronaut_install && run_pyronaut_validate_config && run_pyronaut_process && run_pyronaut_test
                      )
                      local result=$?
                      rm -rf "$tmp_dir"
                      return $result
                    }""");
        }

        metadatas.sort(Comparator.comparing(Guide::slug));
        for (Guide metadata : metadatas) {
            List<GuidesOption> guidesOptionList = GuideGenerationUtils.guidesOptions(metadata, LOG);
            bashScript.append("\n");
            for (GuidesOption guidesOption : guidesOptionList) {
                if (pythonTest != isPyronautPython(guidesOption)) {
                    continue;
                }
                String folder = MacroUtils.getSourceDir(metadata.slug(), guidesOption);
                BuildTool buildTool = guidesOption.getBuildTool();
                if (metadata.apps().stream().anyMatch(app -> app.name().equals(guidesConfiguration.getDefaultAppName()))) {
                    if (GuideUtils.shouldSkip(metadata, buildTool, guidesOption.getLanguage())) {
                        continue;
                    }
                    Optional<App> appOptional = metadata.apps().stream().filter(app -> app.name().equals(guidesConfiguration.getDefaultAppName())).findFirst();
                    if (appOptional.isPresent()) {
                        App defaultApp = appOptional.get();
                        if (!nativeTest || supportsNativeTest(defaultApp, guidesOption)) {
                            List<String> features = GuideUtils.getAppFeatures(defaultApp, guidesOption.getLanguage());
                            if (!folder.contains("-maven-groovy")) {
                                bashScript.append(scriptForFolder(folder, folder, stopIfFailure, buildTool, features.contains("kapt") && Runtime.getRuntime().version().feature() > 17 && buildTool == GRADLE, nativeTest, defaultApp.validateLicense()));
                            }
                        }
                    }
                } else {
                    bashScript.append("cd " + folder + "\n");
                    for (App app : metadata.apps()) {
                        if (GuideUtils.shouldSkip(metadata, buildTool, guidesOption.getLanguage())) {
                            continue;
                        }
                        if (!nativeTest || supportsNativeTest(app, guidesOption)) {
                            List<String> features = GuideUtils.getAppFeatures(app, guidesOption.getLanguage());
                            if (!folder.contains("-maven-groovy")) {
                                bashScript.append(scriptForFolder(app.name(), folder + "/" + app.name(), stopIfFailure, buildTool, features.contains("kapt") && Runtime.getRuntime().version().feature() > 17 && buildTool == GRADLE, nativeTest, app.validateLicense()));
                            }
                        }
                    }
                    bashScript.append("\ncd ..\n");
                }
            }
        }

        if (!stopIfFailure) {
            bashScript.append("""
                    if [ ${#FAILED_PROJECTS[@]} -ne 0 ]; then
                      echo ""
                      echo "-------------------------------------------------"
                      echo "Projects with errors:"
                      for p in `echo ${FAILED_PROJECTS[@]}`; do
                        echo "  $p"
                      done;
                      echo "-------------------------------------------------"
                      exit 1
                    else
                      exit 0
                    fi

                    """);
        }

        return bashScript.toString();
    }

    private static boolean isPyronautPython(GuidesOption guidesOption) {
        return guidesOption.getBuildTool() == PYRONAUT && guidesOption.getLanguage() == Language.PYTHON;
    }
}
