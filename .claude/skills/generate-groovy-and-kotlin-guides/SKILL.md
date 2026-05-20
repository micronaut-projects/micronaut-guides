---
name: generate-groovy-and-kotlin-guides
description: Generates idiomatic Groovy and Kotlin versions of a Micronaut guide from its Java source. Use when a guide has Java code but is missing Groovy or Kotlin implementations. Groovy tests use Spock Framework; Java and Kotlin tests use JUnit 5.
---

This repository contains tutorials for Micronaut. Tutorials are placed in the `guides` directory.

Tutorials can be written in multiple programming languages: Java, Kotlin, and Groovy.

For a tutorial with a given `slug`, the code is placed in:
- `guides/<slug>/java` — Java source
- `guides/<slug>/kotlin` — Kotlin source
- `guides/<slug>/groovy` — Groovy source

## Instructions

1. Identify the target guide's `slug`.
2. Read all files under `guides/<slug>/java/`.
3. If `guides/<slug>/groovy/` does not exist, generate Groovy code from the Java source:
   - The code should be as idiomatic Groovy as possible.
   - Replace JUnit 5 tests with equivalent Spock Framework specifications.
4. If `guides/<slug>/kotlin/` does not exist, generate Kotlin code from the Java source:
   - The code should be as idiomatic Kotlin as possible.
   - Keep JUnit 5 for tests.
5. Write the generated files into their respective directories, mirroring the same package and file structure as the Java source.
