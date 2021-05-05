package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.starter.options.BuildTool

@CompileStatic
class TestScriptGenerator {

    static String generateScript(File guidesFolder, String metadataConfigName, boolean stopIfFailure) {
        String bashScript = '''\
#!/usr/bin/env bash
set -e

FAILED_PROJECTS=()
EXIT_STATUS=0
'''
        List<GuideMetadata> metadatas = GuideProjectGenerator.parseGuidesMetadata(guidesFolder, metadataConfigName)
        for (GuideMetadata metadata : metadatas) {
            boolean skip = !(System.getProperty(GuideProjectGenerator.SYS_PROP_MICRONAUT_GUIDE) != null ? System.getProperty(GuideProjectGenerator.SYS_PROP_MICRONAUT_GUIDE) == metadata.slug : true)
            if (skip) {
                continue
            }
            List<GuidesOption> guidesOptionList = GuideProjectGenerator.guidesOptions(metadata)
            for (GuidesOption guidesOption : guidesOptionList) {
                String folder = GuideProjectGenerator.folderName(metadata.slug, guidesOption)
                BuildTool buildTool = folder.contains(BuildTool.MAVEN.toString()) ? BuildTool.MAVEN : BuildTool.GRADLE
                if (buildTool == BuildTool.MAVEN && metadata.skipMavenTests) {
                    continue
                }
                if (buildTool == BuildTool.GRADLE && metadata.skipGradleTests) {
                    continue
                }
                if (metadata.apps.any { it.name == GuideProjectGenerator.DEFAULT_APP_NAME } ) {
                    bashScript += scriptForFolder(folder, folder, stopIfFailure, buildTool)
                } else {
                    bashScript += """\
cd ${folder}
"""
                    for (GuideMetadata.App app: metadata.apps) {
                        bashScript += scriptForFolder(app.name, folder + '/' + app.name, stopIfFailure, buildTool)
                    }
                    bashScript += """\
cd ..
"""
                }
            }
        }

        if (!stopIfFailure) {
            bashScript += '''
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

    static String scriptForFolder(String nestedFolder, String folder, boolean stopIfFailure, BuildTool buildTool) {
        String bashScript = """\
cd ${nestedFolder}
echo "-------------------------------------------------"
echo "Executing '${folder}' tests"
${buildTool == BuildTool.MAVEN ? './mvnw test' : './gradlew test' } || EXIT_STATUS=\$?
cd ..
"""
        if (stopIfFailure) {
            bashScript += """\
if [ \$EXIT_STATUS -ne 0 ]; then
  echo "'${folder}' tests failed => exit \$EXIT_STATUS"
  exit \$EXIT_STATUS
fi
"""
        } else {
            bashScript += """\
if [ \$EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("\${FAILED_PROJECTS[@]}" ${folder})
  echo "'${folder}' tests failed => exit \$EXIT_STATUS"
fi
EXIT_STATUS=0
"""
        }

        bashScript
    }
}
