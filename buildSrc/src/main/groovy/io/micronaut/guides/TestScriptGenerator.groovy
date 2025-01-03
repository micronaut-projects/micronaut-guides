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
                                 boolean nativeTest = false) {
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

        metadatas.sort { it.slug() }
        for (Guide metadata : metadatas) {
            List<GuidesOption> guidesOptionList = GuideProjectGenerator.guidesOptions(metadata)
            bashScript << """\
"""
            for (GuidesOption guidesOption : guidesOptionList) {
                String folder = GuideProjectGenerator.folderName(metadata.slug(), guidesOption)
                BuildTool buildTool = folder.containsIgnoreCase(MAVEN.toString()) ? MAVEN : GRADLE
                if (metadata.apps().any { it.name() == DEFAULT_APP_NAME } ) {
                    if (GuideUtils.shouldSkip(metadata,buildTool)) {
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
                        if (GuideUtils.shouldSkip(metadata,buildTool)) {
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
}
