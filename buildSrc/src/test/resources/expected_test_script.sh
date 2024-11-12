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
cd base-gradle-java
echo "-------------------------------------------------"
echo "Executing 'base-gradle-java' tests"
./gradlew -q check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./gradlew -q stopTestResourcesService > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" base-gradle-java)
  echo "'base-gradle-java' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd base-gradle-groovy
echo "-------------------------------------------------"
echo "Executing 'base-gradle-groovy' tests"
./gradlew -q check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./gradlew -q stopTestResourcesService > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" base-gradle-groovy)
  echo "'base-gradle-groovy' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd base-gradle-kotlin
echo "-------------------------------------------------"
echo "Executing 'base-gradle-kotlin' tests"
./gradlew -q check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./gradlew -q stopTestResourcesService > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" base-gradle-kotlin)
  echo "'base-gradle-kotlin' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd base-maven-java
echo "-------------------------------------------------"
echo "Executing 'base-maven-java' tests"
./mvnw -q test spotless:check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./mvnw -q mn:stop-testresources-service > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" base-maven-java)
  echo "'base-maven-java' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd base-maven-kotlin
echo "-------------------------------------------------"
echo "Executing 'base-maven-kotlin' tests"
./mvnw -q test spotless:check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./mvnw -q mn:stop-testresources-service > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" base-maven-kotlin)
  echo "'base-maven-kotlin' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0

cd child-gradle-java
echo "-------------------------------------------------"
echo "Executing 'child-gradle-java' tests"
./gradlew -q check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./gradlew -q stopTestResourcesService > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" child-gradle-java)
  echo "'child-gradle-java' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd child-gradle-groovy
echo "-------------------------------------------------"
echo "Executing 'child-gradle-groovy' tests"
./gradlew -q check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./gradlew -q stopTestResourcesService > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" child-gradle-groovy)
  echo "'child-gradle-groovy' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd child-gradle-kotlin
echo "-------------------------------------------------"
echo "Executing 'child-gradle-kotlin' tests"
./gradlew -q check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./gradlew -q stopTestResourcesService > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" child-gradle-kotlin)
  echo "'child-gradle-kotlin' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd child-maven-java
echo "-------------------------------------------------"
echo "Executing 'child-maven-java' tests"
./mvnw -q test spotless:check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./mvnw -q mn:stop-testresources-service > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" child-maven-java)
  echo "'child-maven-java' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd child-maven-kotlin
echo "-------------------------------------------------"
echo "Executing 'child-maven-kotlin' tests"
./mvnw -q test spotless:check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./mvnw -q mn:stop-testresources-service > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" child-maven-kotlin)
  echo "'child-maven-kotlin' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0

cd creating-your-first-micronaut-app-gradle-java
echo "-------------------------------------------------"
echo "Executing 'creating-your-first-micronaut-app-gradle-java' tests"
./gradlew -q check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./gradlew -q stopTestResourcesService > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" creating-your-first-micronaut-app-gradle-java)
  echo "'creating-your-first-micronaut-app-gradle-java' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd creating-your-first-micronaut-app-gradle-groovy
echo "-------------------------------------------------"
echo "Executing 'creating-your-first-micronaut-app-gradle-groovy' tests"
./gradlew -q check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./gradlew -q stopTestResourcesService > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" creating-your-first-micronaut-app-gradle-groovy)
  echo "'creating-your-first-micronaut-app-gradle-groovy' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd creating-your-first-micronaut-app-gradle-kotlin
echo "-------------------------------------------------"
echo "Executing 'creating-your-first-micronaut-app-gradle-kotlin' tests"
./gradlew -q check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./gradlew -q stopTestResourcesService > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" creating-your-first-micronaut-app-gradle-kotlin)
  echo "'creating-your-first-micronaut-app-gradle-kotlin' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd creating-your-first-micronaut-app-maven-java
echo "-------------------------------------------------"
echo "Executing 'creating-your-first-micronaut-app-maven-java' tests"
./mvnw -q test spotless:check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./mvnw -q mn:stop-testresources-service > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" creating-your-first-micronaut-app-maven-java)
  echo "'creating-your-first-micronaut-app-maven-java' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd creating-your-first-micronaut-app-maven-kotlin
echo "-------------------------------------------------"
echo "Executing 'creating-your-first-micronaut-app-maven-kotlin' tests"
./mvnw -q test spotless:check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./mvnw -q mn:stop-testresources-service > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" creating-your-first-micronaut-app-maven-kotlin)
  echo "'creating-your-first-micronaut-app-maven-kotlin' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0

cd hello-base-gradle-java

cd ..
cd hello-base-gradle-groovy

cd ..
cd hello-base-gradle-kotlin

cd ..
cd hello-base-maven-java

cd ..
cd hello-base-maven-groovy

cd ..
cd hello-base-maven-kotlin

cd ..

cd test-gradle-java
cd springboot
echo "-------------------------------------------------"
echo "Executing 'test-gradle-java/springboot' tests"
./gradlew -q check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./gradlew -q stopTestResourcesService > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" test-gradle-java/springboot)
  echo "'test-gradle-java/springboot' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd micronautframeworkjacksondatabind
echo "-------------------------------------------------"
echo "Executing 'test-gradle-java/micronautframeworkjacksondatabind' tests"
./gradlew -q check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./gradlew -q stopTestResourcesService > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" test-gradle-java/micronautframeworkjacksondatabind)
  echo "'test-gradle-java/micronautframeworkjacksondatabind' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd micronautframeworkserde
echo "-------------------------------------------------"
echo "Executing 'test-gradle-java/micronautframeworkserde' tests"
./gradlew -q check || EXIT_STATUS=$?
echo "Stopping shared test resources service (if created)"
./gradlew -q stopTestResourcesService > /dev/null 2>&1 || true
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" test-gradle-java/micronautframeworkserde)
  echo "'test-gradle-java/micronautframeworkserde' tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0

cd ..
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

