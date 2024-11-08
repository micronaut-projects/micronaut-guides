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
echo "Executing 'base-gradle-java' native tests"
./gradlew nativeTest || EXIT_STATUS=$?
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" base-gradle-java)
  echo "'base-gradle-java' native tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd base-gradle-kotlin
echo "-------------------------------------------------"
echo "Executing 'base-gradle-kotlin' native tests"
./gradlew nativeTest || EXIT_STATUS=$?
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" base-gradle-kotlin)
  echo "'base-gradle-kotlin' native tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0

cd child-gradle-java
echo "-------------------------------------------------"
echo "Executing 'child-gradle-java' native tests"
./gradlew nativeTest || EXIT_STATUS=$?
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" child-gradle-java)
  echo "'child-gradle-java' native tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd child-gradle-kotlin
echo "-------------------------------------------------"
echo "Executing 'child-gradle-kotlin' native tests"
./gradlew nativeTest || EXIT_STATUS=$?
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" child-gradle-kotlin)
  echo "'child-gradle-kotlin' native tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0

cd creating-your-first-micronaut-app-gradle-java
echo "-------------------------------------------------"
echo "Executing 'creating-your-first-micronaut-app-gradle-java' native tests"
./gradlew nativeTest || EXIT_STATUS=$?
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" creating-your-first-micronaut-app-gradle-java)
  echo "'creating-your-first-micronaut-app-gradle-java' native tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd creating-your-first-micronaut-app-gradle-kotlin
echo "-------------------------------------------------"
echo "Executing 'creating-your-first-micronaut-app-gradle-kotlin' native tests"
./gradlew nativeTest || EXIT_STATUS=$?
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" creating-your-first-micronaut-app-gradle-kotlin)
  echo "'creating-your-first-micronaut-app-gradle-kotlin' native tests failed => exit $EXIT_STATUS"
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
cd micronautframeworkjacksondatabind
echo "-------------------------------------------------"
echo "Executing 'test-gradle-java/micronautframeworkjacksondatabind' native tests"
./gradlew nativeTest || EXIT_STATUS=$?
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" test-gradle-java/micronautframeworkjacksondatabind)
  echo "'test-gradle-java/micronautframeworkjacksondatabind' native tests failed => exit $EXIT_STATUS"
fi
EXIT_STATUS=0
cd micronautframeworkserde
echo "-------------------------------------------------"
echo "Executing 'test-gradle-java/micronautframeworkserde' native tests"
./gradlew nativeTest || EXIT_STATUS=$?
cd ..
if [ $EXIT_STATUS -ne 0 ]; then
  FAILED_PROJECTS=("${FAILED_PROJECTS[@]}" test-gradle-java/micronautframeworkserde)
  echo "'test-gradle-java/micronautframeworkserde' native tests failed => exit $EXIT_STATUS"
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

