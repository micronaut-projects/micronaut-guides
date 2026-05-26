# MNG-328 Code Review Validation

PR: micronaut-projects/micronaut-guides#1786
Paperclip issue: MNG-328
Linked GitHub issue: micronaut-projects/micronaut-guides#1665
Target branch: master
Head branch: mng-328-add-groovy-rest-api-data

## Commits Reviewed

- 8fac2d9e354aed66bc4a121fb38ff69b99943341 - Add Groovy version for REST API data guide
- a73e62a8185062d24bf04bc4e7b653ae1faabebf - Make REST API data guide wording language-neutral

## Verification

Passed:

- git diff --check
- ./gradlew buildingARestApiSpringBootVsMicronautDataBuild buildingARestApiSpringBootVsMicronautDataRunTestScript --stacktrace -x buildingARestApiSpringBootVsMicronautDataRunNativeTestScript

The Gradle run generated the guide/projects, zips, rendered output, and ran the generated JVM tests for Java and Groovy variants of both sample applications.

Known caveat preserved from QA: the full native-including command ./gradlew buildingARestApiSpringBootVsMicronautDataBuild --stacktrace is locally blocked by a pre-existing generated Java Micronaut native-test GraalVM reachability-metadata schema mismatch before reaching new Groovy native work.

## Reviewer / Project Metadata

- Required type label applied: type: docs.
- Issue creator / reporter login: sdelamo.
- Reviewer request attempted for sdelamo through GitHub REST and rejected with: Review cannot be requested from pull request author.
- Selected Micronaut organization project from QA intake: 5.0.1 Release, project #146.
- Project association attempted through GitHub GraphQL and rejected because the token lacks the project write scope required by addProjectV2ItemById. The token only has read:project for project access.
