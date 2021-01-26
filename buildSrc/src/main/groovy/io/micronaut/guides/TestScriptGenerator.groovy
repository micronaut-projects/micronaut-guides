package io.micronaut.guides

class TestScriptGenerator {

    static String generateScript(File codeDir, boolean stopIfFailure = false) {
        String bashScript = '''\
#!/usr/bin/env bash
set -e

FAILED_PROJECTS=()
EXIT_STATUS=0
'''
        codeDir.eachDir {dir ->
            bashScript += """\
cd ${dir.name}
echo "-------------------------------------------------"
echo "Executing '${dir.name}' tests"
${dir.name.contains('gradle') ? './gradlew -q test' : './mvnw -q test' } || EXIT_STATUS=\$?
cd ..
"""
            if (stopIfFailure) {
                bashScript += """\
if [ \$EXIT_STATUS -ne 0 ]; then
  echo "'${dir.name}' tests failed => exit \$EXIT_STATUS"
  exit \$EXIT_STATUS
fi
"""
            } else {
                bashScript += """\
if [ \$EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("\${FAILED_PROJECTS[@]}" ${dir.name})
  echo "'${dir.name}' tests failed => exit \$EXIT_STATUS"
fi
EXIT_STATUS=0
"""
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
}
