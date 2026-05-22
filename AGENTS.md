# Agent Guidance

This repository publishes the Micronaut Guides site and generated sample
projects. Keep changes focused on guide source, generator code, or the minimum
supporting build logic needed for the issue.

## Scope

- Edit guide source under `guides/<guide-name>/`, shared snippets under
  `src/docs/common/`, generator code under `buildSrc/`, or documented assets.
- Do not commit generated output such as `build/`, `build/dist/`, or
  `src/docs/asciidoc/`.
- Preserve the guide matrix model: language and build-tool variants are
  generated from shared AsciiDoc, metadata, and per-language source trees.
- Keep guide metadata valid against the repository schema and use Starter
  features that are available to the generator.

## Verification

- For one guide, prefer its dynamic Gradle task: convert the kebab-case guide
  directory to lowerCamelCase and append `Build`, for example
  `./gradlew micronautHttpClientBuild`.
- For guide sample execution, use the matching generated run-test task when the
  issue or CI failure is about runtime behavior.
- For generator or schema changes, run the narrowest relevant `buildSrc` tests
  before broad guide builds.
- If a failure reproduces on the target branch without your change, record the
  baseline evidence and keep PR-specific fixes separate from repository-wide
  baseline fixes.

## Pull Requests

- Target `master` unless the issue or maintainer direction names another branch.
- Keep PR bodies clear about which guide variants were verified.
- Do not merge PRs or cut releases from an agent run.
