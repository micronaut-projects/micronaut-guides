package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.guides.core.App
import io.micronaut.guides.core.DefaultGuideParser
import io.micronaut.guides.core.DefaultJsonSchemaProvider
import io.micronaut.guides.core.Guide
import io.micronaut.guides.core.GuideParser
import io.micronaut.guides.core.GuideUtils
import io.micronaut.guides.core.GuidesOption
import io.micronaut.guides.core.JsonSchemaProvider
import io.micronaut.json.JsonMapper

import java.util.stream.Collectors
import static io.micronaut.guides.GuideProjectGenerator.DEFAULT_APP_NAME
import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.MAVEN
import static io.micronaut.starter.options.BuildTool.PYRONAUT
import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.BuildTool

@CompileStatic
class TestScriptGenerator {

    public static final String GITHUB_WORKFLOW_JAVA_CI = 'Java CI'
    public static final String ENV_GITHUB_WORKFLOW = 'GITHUB_WORKFLOW'
    public static final String EMPTY_SCRIPT = '''\
#!/usr/bin/env bash
set -e
exit 0
'''

    private static List<String> guidesChanged(List<String> changedFiles) {
        changedFiles.findAll { path ->
            path.startsWith('guides')
        }.collect { path ->
            String guideFolder = path.substring('guides/'.length())
            guideFolder.substring(0, guideFolder.indexOf('/'))
        }.unique()
    }

    private static boolean changesMicronautVersion(List<String> changedFiles) {
        changedFiles.any { it.contains("version.txt") }
    }

    private static boolean changesDependencies(List<String> changedFiles, List<String> changedGuides) {
        if (changedGuides) {
            return false
        }
        changedFiles.any { it.contains("pom.xml") }
    }

    private static boolean changesBuildScr(List<String> changedFiles) {
        changedFiles.any { it.contains('buildSrc') }
    }

    private static boolean shouldSkip(Guide metadata,
                                      List<String> guidesChanged,
                                      boolean forceExecuteEveryTest) {

        if (!Utils.process(metadata)) {
            return true
        }

        if (forceExecuteEveryTest) {
            return false
        }

        return !guidesChanged.contains(metadata.slug())
    }

    static String generateScript(File guidesFolder,
                                 String metadataConfigName,
                                 boolean stopIfFailure,
                                 List<String> changedFiles) {
        List<String> slugsChanged = guidesChanged(changedFiles)
        boolean forceExecuteEveryTest = changesMicronautVersion(changedFiles) ||
                changesDependencies(changedFiles, slugsChanged) ||
                changesBuildScr(changedFiles) ||
                (System.getenv(ENV_GITHUB_WORKFLOW) && System.getenv(ENV_GITHUB_WORKFLOW) != GITHUB_WORKFLOW_JAVA_CI) ||
                (!changedFiles && !System.getenv(ENV_GITHUB_WORKFLOW))

        //TODO. We should have an application context and get it from it.
        JsonMapper jsonMapper = JsonMapper.createDefault();
        JsonSchemaProvider jsonSchemaProvider = new DefaultJsonSchemaProvider();
        GuideParser guideParser = new DefaultGuideParser(jsonSchemaProvider, jsonMapper);
        List<Guide> metadatas = guideParser.parseGuidesMetadata(guidesFolder, metadataConfigName)
        metadatas = metadatas.stream()
                .filter(metadata -> !shouldSkip(metadata, slugsChanged, forceExecuteEveryTest))
                .collect(Collectors.toList())
        generateScript(metadatas, stopIfFailure)
    }

    static void generateTestScript(File output,
                                   List<Guide> metadatas,
                                   boolean stopIfFailure) {
        String script = generateScript(metadatas, stopIfFailure)
        generateTestScript(output, script)
    }

    static void generateNativeTestScript(File output,
                                   List<Guide> metadatas,
                                   boolean stopIfFailure) {
        String script = generateScript(metadatas, stopIfFailure, true)
        generateTestScript(output, script, 'native-test.sh')
    }

    static void generatePythonTestScript(File output,
                                         List<Guide> metadatas,
                                         boolean stopIfFailure) {
        String script = generateScript(metadatas, stopIfFailure, false, true)
        generateTestScript(output, script, 'python-test.sh')
    }

    static void generateTestScript(File output, String script, String scriptFileName = "test.sh") {
        File testScript = new File(output, scriptFileName)
        testScript.createNewFile()
        testScript.text = script
        testScript.executable = true
    }

    static boolean supportsNativeTest(App app, GuidesOption guidesOption) {
        isMicronautFramework(app) &&
        guidesOption.buildTool.isGradle() && // right now we don't support Maven native tests without adding a profile
        supportsNativeTest(guidesOption.language) &&
        guidesOption.testFramework == TestFramework.JUNIT

    }

    static boolean isMicronautFramework(App app) {
        !app.framework() || app.framework() == "Micronaut"
    }

    static boolean supportsNativeTest(Language language) {
        language != Language.GROOVY
    }

    static String generateScript(List<Guide> metadatas,
                                 boolean stopIfFailure,
                                 boolean nativeTest = false,
                                 boolean pythonTest = false) {
        StringBuilder bashScript = new StringBuilder('''\
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
}
''')
        if (pythonTest) {
            bashScript << '''\

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

set_pyronaut_project_venv () {
  if [ -e .venv ] || [ -L .venv ] || [ -z "${VIRTUAL_ENV:-}" ] || [ ! -x "$VIRTUAL_ENV/bin/python" ]; then
    return 0
  fi
  ln -s "$VIRTUAL_ENV" .venv
}

pyronaut_dependency_cache () {
  echo "${PYRONAUT_DEPENDENCY_CACHE:-.pyronaut-m2}"
}

run_pyronaut_install () {
  local install_args=()
  if [ -n "${PYRONAUT_LOCAL_REPOSITORY:-}" ]; then
    install_args+=(--local-repository "$(pyronaut_dependency_cache)")
    if [ "${PYRONAUT_REFRESH_DEPENDENCIES:-true}" = "true" ]; then
      install_args+=(--refresh)
    fi
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
    test_args+=(--local-repository "$(pyronaut_dependency_cache)")
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
}
'''
        }

        metadatas.sort { it.slug() }
        for (Guide metadata : metadatas) {
            List<GuidesOption> guidesOptionList = GuideProjectGenerator.guidesOptions(metadata)
            bashScript << """\
"""
            for (GuidesOption guidesOption : guidesOptionList) {
                if (pythonTest != isPyronautPython(guidesOption)) {
                    continue
                }
                String folder = GuideProjectGenerator.folderName(metadata.slug(), guidesOption)
                BuildTool buildTool = guidesOption.buildTool
                if (metadata.apps().any { it.name() == DEFAULT_APP_NAME } ) {
                    if (GuideUtils.shouldSkip(metadata,buildTool, guidesOption.getLanguage())) {
                        continue
                    }
                    def defaultApp = metadata.apps().find { it.name() == DEFAULT_APP_NAME }
                    if (!nativeTest || supportsNativeTest(defaultApp, guidesOption)) {
                        def features = GuideUtils.getAppFeatures(defaultApp,guidesOption.language)
                        if (!folder.contains("-maven-groovy")) {
                            bashScript << scriptForFolder(folder, folder, stopIfFailure, buildTool, features.contains("kapt") && Runtime.version().feature() > 17 && buildTool == GRADLE, nativeTest, defaultApp.validateLicense())
                        }
                    }
                } else {
                    bashScript << """\
cd $folder
"""
                    for (App app : metadata.apps()) {
                        if (GuideUtils.shouldSkip(metadata,buildTool, guidesOption.getLanguage())) {
                            continue
                        }
                        if (!nativeTest || supportsNativeTest(app, guidesOption)) {
                            def features = GuideUtils.getAppFeatures(app,guidesOption.language)
                            if (!folder.contains("-maven-groovy")) {
                                bashScript << scriptForFolder(app.name(), folder + '/' + app.name(), stopIfFailure, buildTool, features.contains("kapt") && Runtime.version().feature() > 17 && buildTool == GRADLE, nativeTest, app.validateLicense())
                            }
                        }
                    }
                    bashScript << """\
cd ..
"""
                }
            }
        }

        if (!stopIfFailure) {
            bashScript << '''
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

'''
        }

        bashScript
    }

    private static String scriptForFolder(String nestedFolder,
                                          String folder,
                                          boolean stopIfFailure,
                                          BuildTool buildTool,
                                          boolean noDaemon,
                                          boolean nativeTest,
                                          boolean validateLicense) {
        String testcopy = nativeTest ? "native tests" : "tests"
        String bashScript = """\
cd $nestedFolder
echo "-------------------------------------------------"
echo "Executing '$folder' $testcopy"
"""
if (noDaemon) {
    bashScript += "kill_kotlin_daemon\n"
}
if (nativeTest) {
bashScript += """\
${buildTool == MAVEN ? './mvnw -Pnative test' : './gradlew nativeTest'} || EXIT_STATUS=\$?
"""
} else if (buildTool == PYRONAUT) {
bashScript += """\
run_pyronaut_tests || EXIT_STATUS=\$?
"""
} else {
String mavenCommand = validateLicense ? './mvnw -q test spotless:check' : './mvnw -q test'
bashScript += """\
${buildTool == MAVEN ? mavenCommand : './gradlew -q check' } || EXIT_STATUS=\$?
echo "Stopping shared test resources service (if created)"
${buildTool == MAVEN ? './mvnw -q mn:stop-testresources-service' : './gradlew -q stopTestResourcesService'} > /dev/null 2>&1 || true
"""
}
if (noDaemon) {
    bashScript += "kill_kotlin_daemon\n"
}
bashScript += """\
cd ..
"""
        if (stopIfFailure) {
            bashScript += """\
if [ \$EXIT_STATUS -ne 0 ]; then
  echo "'$folder' $testcopy failed => exit \$EXIT_STATUS"
  exit \$EXIT_STATUS
fi
"""
        } else {
            bashScript += """\
if [ \$EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("\${FAILED_PROJECTS[@]}" $folder)
  echo "'$folder' $testcopy failed => exit \$EXIT_STATUS"
fi
EXIT_STATUS=0
"""
        }

        bashScript
    }

    private static boolean isPyronautPython(GuidesOption guidesOption) {
        guidesOption.buildTool == PYRONAUT && guidesOption.language == Language.PYTHON
    }
}
