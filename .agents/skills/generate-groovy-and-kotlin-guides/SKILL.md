---
name: generate-groovy-and-kotlin-guides
description: Generate idiomatic Groovy and Kotlin versions of a Micronaut guide from its Java source. Use when a guide has Java code but is missing Groovy or Kotlin implementations; Groovy tests use Spock Framework, while Java and Kotlin tests use JUnit 5.
---

# Generate Groovy and Kotlin Guides

Use this skill in the Micronaut guides repository when a guide has a Java implementation under `guides/<slug>/java/` and needs matching Groovy and/or Kotlin implementations.

## Guide Layout

Tutorials are placed in the `guides` directory and can support Java, Kotlin, and Groovy:

```text
guides/<slug>/java
guides/<slug>/kotlin
guides/<slug>/groovy
```

Generated language directories should mirror the Java package and file structure unless an existing guide pattern for the same topic requires a different layout.

Full Java, Groovy, and Kotlin support is the metadata default. Do not add `"languages": ["JAVA", "GROOVY", "KOTLIN"]` when all three language directories exist.

## Procedure

1. Identify the target guide `slug`.
2. Read all files under `guides/<slug>/java/` and any shared guide resources under `guides/<slug>/src/`.
3. If `guides/<slug>/groovy/` does not exist, generate Groovy code from the Java source:
   - Use idiomatic Groovy.
   - Replace JUnit 5 tests with equivalent Spock Framework specifications.
   - Use `Spec.groovy` naming for Groovy tests unless nearby guides use a more specific convention.
4. If `guides/<slug>/kotlin/` does not exist, generate Kotlin code from the Java source:
   - Use idiomatic Kotlin.
   - Keep JUnit 5 for tests.
   - Use `Test.kt` naming for Kotlin tests.
5. Write generated language-specific source and test files into their language directories, preserving package names and source layout.
6. Share identical resources between languages. If Java resources can also be used by Groovy and Kotlin, place them under `guides/<slug>/src/main/resources` or `guides/<slug>/src/test-resources` instead of copying them into each language directory; keep language-specific resource copies only when contents differ.
7. Update guide metadata only if the generated languages should be explicitly listed or if nearby guide patterns require it.
8. Write generated files into their language directories, preserving package names and source/resource layout.
9. Update guide metadata only when the guide intentionally supports a subset of languages. Omit `languages` when the guide has Java, Groovy, and Kotlin because that is the default.

## Checks

After generating files, compare with a nearby multi-language guide and run the guide-specific validation task when possible. If validation is too broad for the requested change, at minimum inspect the generated source, tests, imports, and package declarations for all created files.
