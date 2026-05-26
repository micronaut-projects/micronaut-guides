---
name: micronaut-guides-authoring
description: Use when creating or updating Micronaut guides in this repository, including guides/*/metadata.json, guide Asciidoc files, language-specific sample code, resources, common snippets, callouts, tags, categories, and multi-application guide layout.
---

# Micronaut Guides Authoring

## Start Here

Use this skill for guide content and sample app changes under `guides/` and `src/docs/common/`.

Before editing, inspect at least one nearby guide with the same topic shape:

```bash
rg -n '"categories"|applicationType|base|languages|buildTools|testFramework|skipGradleTests|skipMavenTests' guides/<candidate-guide>/metadata.json guides
find guides/<candidate-guide> -type f | sort
```

Prefer copying established local patterns over inventing a new structure.

## Guide Layout

Single-application guides use:

```text
guides/<slug>/
  metadata.json
  <slug>.adoc
  java/src/main/java/example/micronaut/...
  java/src/test/java/example/micronaut/...
  groovy/src/main/groovy/example/micronaut/...
  groovy/src/test/groovy/example/micronaut/...Spec.groovy
  kotlin/src/main/kotlin/example/micronaut/...
  kotlin/src/test/kotlin/example/micronaut/...Test.kt
  src/main/resources/...
  src/test-resources/...
```

Multi-application guides add an app directory first:

```text
guides/<slug>/<app-name>/<language>/src/...
```

App names must match `metadata.json` `apps[].name`. Single-app guides use `"default"`.

## Metadata

Use the current schema in `buildSrc/src/main/resources/guide-metadata.schema.json`. Required fields are:

- `title`
- `intro`
- `authors`
- `categories`
- `publicationDate`

Use `categories`, an array of display strings from `buildSrc/src/main/java/io/micronaut/guides/Category.java`; do not use the older singular `category` field.

Each app entry usually includes:

```json
{
  "name": "default",
  "features": ["serialization-jackson"]
}
```

Feature names must be valid Micronaut Starter features. If a guide needs a feature not provided by Starter, add a feature class under `buildSrc/src/main/java/io/micronaut/guides/feature` and keep dependency coordinates in `buildSrc/src/main/resources/pom.xml`.

Use optional metadata only when needed: `languages`, `buildTools`, `testFramework`, `minimumJavaVersion`, `maximumJavaVersion`, `cloud`, `publish`, `base`, `zipIncludes`, `skipGradleTests`, `skipMavenTests`, `env`, app-specific `javaFeatures`, `kotlinFeatures`, `groovyFeatures`, `invisibleFeatures`, `excludeTest`, and `excludeSource`.

Omit `languages` when a guide supports all default languages: Java, Groovy, and Kotlin. Add `languages` only when the guide intentionally narrows support to a subset.

## Asciidoc

The guide file normally matches the directory name: `guides/<slug>/<slug>.adoc`.

Start with the standard common snippets unless the guide has a reason to vary:

```asciidoc
common:header.adoc[]
common:requirements.adoc[]
common:completesolution.adoc[]
common:create-app.adoc[]
```

Use repository macros so one guide renders for all build tools and languages:

- `common:<file>.adoc[]` for snippets in `src/docs/common/snippets/common-<file>.adoc`.
- `source:ClassName[]` for main source files.
- `test:ClassNameTest[]` for tests; Groovy resolves to `Spec` when Spock is used.
- `rawTest:ClassNameTest[]` when the exact test name should be used.
- `resource:file.yml[]` for `src/main/resources`.
- `testResource:file.yml[]` for test resources.
- `callout:name[]` for `src/docs/common/callouts/callout-name.adoc`.
- `dependency:artifact-id[]` and `:dependencies:` blocks for build-tool-specific dependency snippets.
- `zipInclude:path[]`, `guideLink:slug[]`, and `diffLink:` only when matching existing examples.

Use conditional blocks for variant-specific text:

```asciidoc
:exclude-for-languages:groovy
Text not rendered for Groovy.
:exclude-for-languages:

:exclude-for-build:maven
Gradle-only text.
:exclude-for-build:

:exclude-for-jdk-lower-than:21
Text hidden when the guide minimum JDK is lower than 21.
:exclude-for-jdk-lower-than:
```

Place numbered callout explanations immediately after the macro that emits the snippet, and keep callout numbers aligned with tags or comments in the source files.

## Source Code

Keep example package names as `example.micronaut` unless metadata explicitly uses another `packageName`.

When changing guide code, update all supported languages. If `languages` is absent, the supported languages are the default Java, Groovy, and Kotlin; if present, treat it as an intentional narrowed subset. Java and Kotlin tests normally end in `Test`; Groovy tests normally end in `Spec`.

Shared resource overrides can live directly under `guides/<slug>/src/...`; language-specific overrides go under the language directory. For multi-app guides, apply the same rule inside each app directory.

Prefer shared resources when supported languages need the same file. Put common files such as `src/main/resources/logback.xml`, `src/main/resources/application.yml`, and `src/test-resources/...` at the guide or app root instead of duplicating them under `java/`, `groovy/`, and `kotlin/`; keep language-specific resource copies only when their contents must differ.

## Final Checks

For a guide slug, validate with the `micronaut-guides-validation` skill. At minimum, run the generated guide build task and inspect generated output in `build/dist` and `build/code` when the change affects rendered content or sample code.
