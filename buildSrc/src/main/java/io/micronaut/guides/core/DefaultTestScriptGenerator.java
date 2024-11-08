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
import java.util.List;
import java.util.stream.Collectors;

import static io.micronaut.starter.options.BuildTool.GRADLE;
import static io.micronaut.starter.options.BuildTool.MAVEN;


@Singleton
public class DefaultTestScriptGenerator implements TestScriptGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTestScriptGenerator.class);

    private GuidesConfiguration guidesConfiguration;
    private GuideParser guideParser;

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
                "cd %s\n" +
                        "echo \"-------------------------------------------------\"\n" +
                        "echo \"Executing '%s' %s\"\n",
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
        } else {
            String mavenCommand = validateLicense ? "./mvnw -q test spotless:check" : "./mvnw -q test";
            bashScript.append(String.format(
                    "%s || EXIT_STATUS=$?\n" +
                            "echo \"Stopping shared test resources service (if created)\"\n" +
                            "%s > /dev/null 2>&1 || true\n",
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
                    "if [ $EXIT_STATUS -ne 0 ]; then\n" +
                            "  echo \"'%s' %s failed => exit $EXIT_STATUS\"\n" +
                            "  exit $EXIT_STATUS\n" +
                            "fi\n",
                    folder, testCopy
            ));
        } else {
            bashScript.append(String.format(
                    "if [ $EXIT_STATUS -ne 0 ]; then\n" +
                            "  FAILED_PROJECTS=(\"${FAILED_PROJECTS[@]}\" %s)\n" +
                            "  echo \"'%s' %s failed => exit $EXIT_STATUS\"\n" +
                            "fi\n" +
                            "EXIT_STATUS=0\n",
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

        metadatas.sort((o1, o2) -> o1.slug().compareTo(o2.slug()));
        for (Guide metadata : metadatas) {
            List<GuidesOption> guidesOptionList = GuideGenerationUtils.guidesOptions(metadata, LOG);
            bashScript.append("\n");
            for (GuidesOption guidesOption : guidesOptionList) {
                String folder = MacroUtils.getSourceDir(metadata.slug(), guidesOption);
                BuildTool buildTool = folder.contains(MAVEN.toString()) ? MAVEN : GRADLE;
                if (metadata.apps().stream().anyMatch(app -> app.name().equals(guidesConfiguration.getDefaultAppName()))) {
                    if (GuideUtils.shouldSkip(metadata, buildTool)) {
                        continue;
                    }
                    App defaultApp = metadata.apps().stream().filter(app -> app.name().equals(guidesConfiguration.getDefaultAppName())).findFirst().get();
                    if (!nativeTest || supportsNativeTest(defaultApp, guidesOption)) {
                        List<String> features = GuideUtils.getAppFeatures(defaultApp, guidesOption.getLanguage());
                        if (!folder.contains("-maven-groovy")) {
                            bashScript.append(scriptForFolder(folder, folder, stopIfFailure, buildTool, features.contains("kapt") && Runtime.getRuntime().version().feature() > 17 && buildTool == GRADLE, nativeTest, defaultApp.validateLicense()));
                        }
                    }
                } else {
                    bashScript.append("cd " + folder + "\n");
                    for (App app : metadata.apps()) {
                        if (GuideUtils.shouldSkip(metadata, buildTool)) {
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
}
