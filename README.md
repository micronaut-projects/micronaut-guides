# Micronaut Guides

This is the main repository for the [Micronaut Guides](https://guides.micronaut.io).

## Build the guides

To build all the guides run:

```shell
$ ./gradlew build
```

This will generate all the projects and guides in `build/dist` and this is what needs to be published to GitHub Pages.

To build a single guide, run the dynamic task created by `GuidesPlugin`; convert the kebab-case guide directory name to lowerCamelCase and add "Build". For example, to build `micronaut-http-client`, run:

```shell
./gradlew micronautHttpClientBuild
```

## Create a new guide

For a high-level overview of the guides infrastructure, take a look at this [blog post](https://micronaut.io/2021/04/12/improving-the-micronaut-guides-infrastructure/).

All the guides leverage [Micronaut Starter](https://github.com/micronaut-projects/micronaut-starter) core to create the projects. The idea is that one guide can generate up to six different projects, one per language (Java, Groovy, and Kotlin) and build tool (Gradle and Maven).

### Guide structure

All the guides are in the `guides` directory in separate subdirectories. Inside each directory, the main file is `metadata.json`, which describes the guide. The metadata schema is defined in [guide-metadata.schema.json](https://github.com/micronaut-projects/micronaut-guides/blob/master/buildSrc/src/main/resources/guide-metadata.schema.json), and parsed guide fields are represented by the [Guide](https://github.com/micronaut-projects/micronaut-guides/blob/master/buildSrc/src/main/java/io/micronaut/guides/core/Guide.java) type.

```json
{
  "title": "Micronaut HTTP Client",
  "intro": "Learn how to use Micronaut low-level HTTP Client. Simplify your code with the declarative HTTP client.",
  "authors": ["Sergio del Amo", "Iván López"],
  "tags": ["client", "rx", "flowable", "json-streams"],
  "categories": ["Getting Started"],
  "publicationDate": "2018-07-02",
  "apps": [
    {
      "name": "default",
      "features": ["graalvm", "reactor"]
    }
  ]
}
```

Besides the obvious fields that do not need further explanation, the others are:

- `tags`: List of tags added to the guide. You don't need to include the language here because it is added automatically when generating the JSON file for the guides webpage.
- `categories`: Needs to contain valid values from the [Category](https://github.com/micronaut-projects/micronaut-guides/blob/master/buildSrc/src/main/java/io/micronaut/guides/Category.java) enum.
- `buildTools`: By default, we generate guide code for Gradle and Maven. If a guide is specific to only one build tool, define it here.
- `languages`: Guides should be written in all three languages. Sometimes a guide is written in only one language or supports only a specific language.
- `testFramework`: By default, Java and Kotlin applications are tested with JUnit 5 and Groovy applications are tested with Spock. In some cases, Java guides are tested with Spock. Use this property to configure it.
- `skipGradleTests`: Set it to `true` to skip running the tests for the Gradle applications for the guide. This is useful when it's not easy to run tests on CI, for example for some cloud guides.
- `skipMavenTests`: Same as `skipGradleTests` but for Maven applications.
- `minimumJavaVersion`: If the guide needs a minimum Java version (for example JDK 17 for Records), define it in this property.
- `maximumJavaVersion`: If the guide needs a maximum Java version (for example JDK 11 for Azure Functions), define it in this property.
- `zipIncludes`: List of additional files to include in the generated zip file for the guide.
- `publish`: defaults to true for regular guides; set to false for partial/base guides
- `base`: defaults to null; if set, indicates directory name of the base guide to copy before copying the current
- `apps`: List of `name`-`features` pairs for the generated application. There are two types of guides. Most guides generate only one application (single-app). In this case, the application name needs to be `default`. A few guides generate multiple applications, so they need to be declared here:
```json
  ...
  "apps": [
    {
      "name": "bookcatalogue",
      "features": ["tracing-jaeger", "management"]
    },
    {
      "name": "bookinventory",
      "features": ["tracing-jaeger", "management"]
    },
    {
      "name": "bookrecommendation",
      "features": ["tracing-jaeger", "management", "reactor"]
    }
  ]
```
The features need to be **valid** Starter features because the list is used directly when generating applications with Starter infrastructure. If you need a feature that is not available in Starter, create it in `buildSrc/src/main/java/io/micronaut/guides/feature`. Also declare the GAV coordinates and version in `buildSrc/src/main/resources/pom.xml`. Dependabot is configured in this project to look for that file and send pull requests to update the dependencies.

Inside the specific guide directory, there should be a directory per language with the appropriate directory structure. All these files are copied into the final guide directory after the guide is generated.

```shell
micronaut-http-client
├── groovy
│     └── src
│         ├── main
│         │     └── groovy
│         │         └── example
│         │             └── micronaut
│         └── test
│             └── groovy
│                 └── example
│                     └── micronaut
├── java
│     └── src
│         ├── main
│         │     └── java
│         │         └── example
│         │             └── micronaut
│         └── test
│             └── java
│                 └── example
│                     └── micronaut
├── kotlin
│     └── src
│         ├── main
│         │     └── kotlin
│         │         └── example
│         │             └── micronaut
│         └── test
│             └── kotlin
│                 └── example
│                     └── micronaut
└── src
    └── main
        └── resources
```

For multi-application guides, there needs to be an additional directory with the name of the application declared in the `metadata.json` file:

```shell
micronaut-microservices-distributed-tracing-zipkin
├── bookcatalogue
│     ├── groovy
│     │    ...
│     ├── java
│     │    ...
│     └── kotlin
│          ...
├── bookinventory
│     ├── groovy
│     │    ...
│     ├── java
│     │    ...
│     └── kotlin
│          ...
└── bookrecommendation
      ├── groovy
      │    ...
      ├── java
      │    ...
      └── kotlin
```

### Writing the guide

There is only one Asciidoctor file per guide in the guide root directory (sibling to `metadata.json`). This file is used to generate all combinations for the guide (language and build tool), so account for that when writing the guide. Name the Asciidoctor file the same as the directory, with an "adoc" extension, e.g. `micronaut-http-client.adoc` for the `micronaut-http-client` guide directory.

We do not write a fully valid Asciidoctor file directly; instead, guide source uses repository-specific macros. During the build process, the final HTML for the guide is rendered in two phases. In the first phase, custom macros and includes are evaluated to generate a language-build tool version of the guide in `src/docs/asciidoc`. This directory is excluded from source control and should be considered temporary. Then the final HTML for the generated guide variants is rendered from that valid Asciidoctor file.

#### Placeholders

You can use the following placeholders while writing a guide:

* `@language@`
* `@guideTitle@`
* `@guideIntro@`
* `@micronaut@`
* `@lang@`
* `@build@`
* `@testFramework@`
* `@authors@`
* `@languageextension@`
* `@testsuffix@`
* `@sourceDir@`
* `@minJdk@`
* `@api@`
* `@features@`
* `@features-words@`

#### Common snippets

We have small pieces of text that are used in different guides. To avoid duplication, we have common snippets in the `src/docs/common` directory. For example, the file `common-header-top.adoc`:

```asciidoc
= @guideTitle@

@guideIntro@

Authors: @authors@

Micronaut Version: @micronaut@
```

will render the title, description, authors, and version of all the guides. The variables defined between `@` signs are evaluated and replaced during the first stage of the Asciidoctor render. For example, for the Micronaut HTTP Client guide, the previous common snippet generates:

```asciidoc
// Start: common-header-top.adoc
= Micronaut HTTP Client

Learn how to use Micronaut low-level HTTP Client. Simplify your code with the declarative HTTP client.

Authors: Sergio del Amo, Iván López

Micronaut Version: 3.2.7

// End: common-header-top.adoc
```

#### Custom macros

There are a number of custom macros available to make it easy to write a single Asciidoctor file for all guide variants and include the necessary source files and resources. This is important because, when we include a source code snippet, the base directory changes for every language the guide supports.

The following snippet from the HTTP Client guide:

```asciidoc
source:GithubConfiguration[]
```

generates the following Asciidoctor depending on the language of the guide:

- Java:

```asciidoc
[source,java]
.src/main/java/example/micronaut/GithubConfiguration.java
----
include::{sourceDir}/micronaut-http-client-gradle-java/src/main/java/example/micronaut/GithubConfiguration.java[]
----
```

- Groovy:
```asciidoc
[source,groovy]
.src/main/groovy/example/micronaut/GithubConfiguration.groovy
----
include::{sourceDir}/micronaut-http-client-gradle-groovy/src/main/groovy/example/micronaut/GithubConfiguration.groovy[]
----
```

- Kotlin:
```asciidoc
[source,kotlin]
.src/main/kotlin/example/micronaut/GithubConfiguration.kt
----
include::{sourceDir}/micronaut-http-client-gradle-kotlin/src/main/kotlin/example/micronaut/GithubConfiguration.kt[]
----
```

As you can see, the macro takes care of the directories (`src/main/java` vs `src/main/groovy` vs `src/main/kotlin`) and the file extension.

Following this same approach there are macros like:
- `source`: Already explained.
- `resource`: To include a file from the `src/main/resources` directory.
- `test`: To include a file from the `src/main/test` directory. This macro also takes care of the suffix depending on the test framework. For example, with `test:GithubControllerTest[]` the macro will reference the file `GithubControllerTest.java` (or .kt) for Java and Kotlin and `GithubControllerSpec.groovy` for Groovy.
- `testResource`: To include a file from the `src/main/test/resources` directory.
- `callout`: To include a common callout snippet.

In all the cases it is possible to pass additional parameters to the macros to customise them. For example, to extract a custom tag from a snippet, we can do `resource:application.yml[tag=githubconfig]`. Look for usages of those macros in the `guides` directory to find more examples.

#### Special custom blocks

There are also special custom blocks to exclude content from the generated guide based on a condition. This is useful when explaining something specific to a build tool, such as how to run tests with Gradle or Maven, or when excluding content based on the language, such as not rendering the GraalVM section in Groovy guides because Groovy is not compatible with GraalVM.

Example:

```asciidoc
:exclude-for-languages:kotlin
<2> The Micronaut framework will not load the bean unless configuration properties are set.
:exclude-for-languages:

:exclude-for-languages:java,groovy
<2> Kotlin doesn't support runtime repeatable annotations (see https://youtrack.jetbrains.com/issue/KT-12794[KT-12794]. We use a custom condition to enable the bean where appropriate.
:exclude-for-languages:
```

For Java and Groovy guides the first block will be included. For Kotlin guide, the second block will be included.

Example for build tool:

```asciidoc
:exclude-for-build:maven

Now start the application. Execute the `./gradlew run` command, which will start the application on port 8080.

:exclude-for-build:

:exclude-for-build:gradle

Now start the application. Execute the `./mvnw mn:run` command, which will start the application on port 8080.

:exclude-for-build:
```

For a Gradle guide, the first block will be included. For a Maven guide, the second one will be included.

As before, look for usages of the macro in the `guides` directory for more examples.

### New Guide Template

To create a new guide, use the following template as the base Asciidoctor file:

```asciidoc
common:header.adoc[]

common:requirements.adoc[]

common:completesolution.adoc[]

common:create-app.adoc[]

TODO: Describe step by step how the user writes the app. Use includes to reference real code:

Example of a Controller

source:HelloController[]

Example of a Test

test:HelloControllerTest[]

common:testApp.adoc[]

common:runapp.adoc[]

common:graal-with-plugins.adoc[]

:exclude-for-languages:groovy

TODO describe how you consume the endpoints exposed by the native executable with curl

:exclude-for-languages:

TODO: Use the generic next step.

common:next.adoc[]

TODO: Or use a personalized section for the guide:

== Next Steps

TODO: link to the documentation modules you used in the guide

```

### Testing the guide

When working on a new guide, generate it as explained before. The guide will be available in the `build/dist` directory, and the applications will be in the `build/code` directory. You can open any directory in `build/code` directly in your IDE to make changes, but remember to copy the code back to the appropriate directory.

In the `build/code` directory, a `test.sh` file is created to run all tests for the generated guides. Run it locally to make sure it passes before submitting a new pull request.

You can run this test with a Gradle task:

```bash
./gradlew :____RunTestScript
```

where `____` is the camel-case name of your guide. For example:

```bash
./gradlew micronautFlywayRunTestScript
```

to run all the tests for the `micronaut-flyway` guide.

## Upgrade Micronaut version

When a new Micronaut version is released, update the [version.txt](https://github.com/micronaut-projects/micronaut-guides/blob/master/version.txt) file in the root directory. Submit a new pull request and if the build passes, merge it. A few minutes later all the guides will be upgraded to the new version.

## Deployment

Guides are published to [gh-pages](https://pages.github.com) following the same branch structure as Micronaut Core:

- One directory per Micronaut minor version: `3.0.x`, `3.1.x`, `3.2.x`,...
- One directory with the latest version of the guide: `latest`

## GitHub Actions

There are two main jobs:

- Java CI: Runs every time we send a pull request or merge something into `master`. The `test.sh` script explained before is executed.
- Java CI SNAPSHOT: Runs a daily cron job that tests the new Micronaut patch and minor versions to make sure everything works when we release new versions in the future.
