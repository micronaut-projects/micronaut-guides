---
name: micronaut-guides-validation
description: Use when building, rendering, testing, or troubleshooting Micronaut guides in this repository, including dynamic Gradle guide tasks, generated build/code and build/dist output, guide test scripts, Docker/Testcontainers checks, and validation after guide edits.
---

# Micronaut Guides Validation

## Task Names

Guide tasks are generated from the guide slug by converting kebab-case to lowerCamelCase.

Examples:

- `micronaut-http-client` -> `micronautHttpClientBuild`
- `micronaut-flyway` -> `micronautFlywayRunTestScript`

If unsure, list the guide task group:

```bash
./gradlew tasks --group "guides <slug>"
```

## Build A Guide

For content or sample-code edits, build the specific guide first:

```bash
./gradlew <lowerCamelSlug>Build --stacktrace
```

This generates rendered guide output under `build/dist` and sample applications under `build/code`.

For infrastructure changes that may affect many guides, run focused guide builds first, then broaden to:

```bash
./gradlew build --stacktrace
```

## Run Guide Tests

After a guide build, run the generated script for that guide:

```bash
./gradlew <lowerCamelSlug>RunTestScript --stacktrace
```

For native-image guide paths, use:

```bash
./gradlew <lowerCamelSlug>RunNativeTestScript --stacktrace
```

The generated shell scripts are under `build/code`. Inspect them when failures are unclear.

## Inspect Output

When validating Asciidoc or macros, inspect generated files:

```bash
find build/code -maxdepth 2 -type d -name '<slug>*' | sort
find build/dist -maxdepth 2 -type f -name '*<slug>*' | sort
rg -n 'TODO|include::|@language@|@guideTitle@|@features@|<1>|ERROR' build/dist build/docs/asciidoc build/code
```

Rendered source includes should point into generated `build/code/<slug>-<build>-<language>/...` projects, not directly into `guides/`.

## Docker And External Services

Some guide tests use Testcontainers or cloud-specific configuration. Before spending time on a failure, check whether the guide metadata skips tests and whether Docker is available:

```bash
sed -n '1,220p' guides/<slug>/metadata.json
docker info
```

If Docker or cloud credentials are unavailable, report that as an environment blocker and still validate rendering with `<lowerCamelSlug>Build` when possible.

## Failure Triage

Use the failing generated project as the debugging target, then copy the fix back to `guides/<slug>/...`.

Common checks:

- Missing macro target: verify source/test/resource path and language-specific extension.
- Groovy test name mismatch: `test:FooTest[]` maps to `FooSpec.groovy` for Spock.
- Metadata validation failure: check `buildSrc/src/main/resources/guide-metadata.schema.json`.
- Unknown category: use display strings from `buildSrc/src/main/java/io/micronaut/guides/Category.java`.
- Unknown feature: search Starter features and local `buildSrc/src/main/java/io/micronaut/guides/feature`.
- Generated code differs by build tool or language: inspect the specific directory under `build/code`.

## Final Report

Report the exact Gradle tasks run and whether they passed. If a validation step is skipped, state the concrete reason, such as missing Docker, missing cloud credentials, or a long-running full build left for CI.
