---
name: micronaut-guides-infrastructure
description: Use when modifying the Micronaut Guides build infrastructure, including buildSrc guide generators, macro substitutions, metadata parsing/schema, categories, generated Gradle tasks, guide features, dependency coordinate replacement, indexing, feeds, theme processing, and the cli module.
---

# Micronaut Guides Infrastructure

## Scope

Use this skill for changes outside normal guide content, especially:

- `buildSrc/src/main/groovy/io/micronaut/guides/**`
- `buildSrc/src/main/java/io/micronaut/guides/**`
- `buildSrc/src/main/resources/guide-metadata.schema.json`
- `buildSrc/src/main/resources/pom.xml`
- root `build.gradle`, `settings.gradle`, and `gradle/asciidoc.gradle`
- `cli/**`
- generated guide task behavior, macros, feed/index output, and feature registration

For ordinary guide content under `guides/`, use `micronaut-guides-authoring` first.

## Key Files

- `GuideAsciidocGenerator.groovy`: custom Asciidoc macros, placeholders, exclusions, and rendered `.adoc` generation.
- `GuidesPlugin.groovy`: dynamic Gradle tasks such as `<slug>Build`, `<slug>GenerateDocs`, `<slug>RunTestScript`, and zip generation.
- `GuideProjectGenerator.groovy` and `core/DefaultGuideProjectGenerator.java`: generated sample project assembly.
- `GuideParser.java`, `Guide.java`, `App.java`, and `guide-metadata.schema.json`: metadata contract.
- `Category.java`: ordered display categories.
- `core/*MacroSubstitution.java`: reusable macro implementations used by tests.
- `feature/*.java`: local Starter feature replacements or additions.
- `buildSrc/src/main/resources/pom.xml`: dependency coordinates used by local features and replacement logic.
- `IndexGenerator.groovy`, `JsonFeedGenerator`, `RssFeedGenerator`, and `ThemeProcessor.groovy`: published site metadata and presentation.

## Change Pattern

1. Find an existing test for the affected behavior under `buildSrc/src/test`.
2. Add or update a focused test before changing generator behavior when possible.
3. Keep macro syntax backward-compatible unless the user explicitly asks for a breaking migration.
4. Update `guide-metadata.schema.json` and metadata parsing together when adding metadata fields.
5. Update `Category.java` only with display strings that should appear on the public guide site.
6. For new local features, add the feature class and ensure dependency coordinates exist in `buildSrc/src/main/resources/pom.xml` when needed.

## Validation

Run focused buildSrc tests for infrastructure changes:

```bash
./gradlew buildSrc:test --tests '<test-class-or-pattern>' --stacktrace
```

If tests touch Gradle task wiring or generated guide output, also build at least one representative guide:

```bash
./gradlew <lowerCamelSlug>Build --stacktrace
```

For broad generator or schema changes, run:

```bash
./gradlew buildSrc:test --stacktrace
./gradlew build --stacktrace
```

Use the `micronaut-guides-validation` skill for guide-specific rendering and generated sample project checks.

## Review Risks

Before finishing, check for:

- Silent changes to all guides caused by macro, placeholder, or template edits.
- Metadata schema drift from parser behavior or tests.
- Task name changes that break documented commands or CI.
- Dependency replacement changes that affect both Gradle and Maven generated projects.
- Generated files or build artifacts accidentally staged outside intended outputs.
