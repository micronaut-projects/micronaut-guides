# Micronaut Guides Agent Guidance

This repository publishes the Micronaut Guides site and the generated sample
applications behind those guides. Keep changes focused on guide content,
guide sample code, or the Gradle/buildSrc infrastructure that renders and tests
them.

## Repository Map

- `guides/<slug>/metadata.json` defines guide metadata and generated app
  features.
- `guides/<slug>/<slug>.adoc` is the source Asciidoc for a guide.
- `guides/<slug>/<language>/src/...` contains language-specific sample code.
  Multi-application guides add the application directory before the language.
- `guides/<slug>/src/...` contains shared resources copied into generated
  sample applications.
- `src/docs/common/` contains reusable snippets and callouts.
- `buildSrc/` contains guide metadata parsing, generated Gradle tasks, feature
  support, theme/index generation, and dependency coordinate replacement.
- `.agents/skills/` contains repo-local skills for guide authoring,
  validation, infrastructure work, and generating Groovy/Kotlin guide variants.

## Guide Authoring

- Use the `micronaut-guides-authoring` skill for changes under `guides/` or
  `src/docs/common/`.
- Inspect nearby guides with the same shape before adding structure. Prefer
  existing metadata, macro, callout, and test patterns over new conventions.
- Use `categories` from `buildSrc/src/main/java/io/micronaut/guides/Category.java`;
  do not reintroduce the older singular `category` field.
- Keep sample package names as `example.micronaut` unless existing metadata
  explicitly uses another package.
- Update every supported language for a guide unless `metadata.json`
  intentionally narrows `languages`.
- Java and Kotlin tests normally use JUnit 5 and end in `Test`; Groovy tests
  normally use Spock and end in `Spec`.
- Feature names in metadata must be valid Micronaut Starter features unless
  the repository adds local feature support under `buildSrc/src/main/java/io/micronaut/guides/feature`
  and dependency coordinates in `buildSrc/src/main/resources/pom.xml`.

## Generated Output

- Treat `build/`, `.gradle/`, `buildSrc/src/generated/`, and generated
  `src/docs/asciidoc/*.adoc` files as build output unless the task explicitly
  asks for generated artifacts.
- When debugging generated projects, inspect `build/code` and `build/dist`,
  then copy the source fix back to `guides/`, `src/docs/common/`, or `buildSrc/`.

## Validation

- Use the `micronaut-guides-validation` skill when rendering or testing guides.
- For guide content or sample-code edits, run the specific dynamic task:
  `./gradlew <lowerCamelSlug>Build --stacktrace`.
- For generated sample tests, run
  `./gradlew <lowerCamelSlug>RunTestScript --stacktrace`.
- For infrastructure changes, start with focused guide builds before broadening
  to `./gradlew build --stacktrace`.
- For documentation-only repository guidance changes, `git diff --check` is
  enough unless the edit changes guide rendering or build logic.
