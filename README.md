# Micronaut Guides

This is the main repository for the [Micronaut Guides](https://guides.micronaut.io). There is also an alternative [guides index](https://guides.micronaut.io/latest/)

## Build the guides

To build all the guides run:

```shell
$ ./gradlew build
```

This will generate all the projects and guides in `build/dist` and this is what needs to be published to GitHub Pages.

To build a single guide, run the dynamic task created by `GuidesPlugin`; convert the kabab case guide directory name to lowerCamelCase and add "Build", e.g. to build `micronaut-http-client`, run

```shell
./gradlew micronautHttpClientBuild
```

## Create a new guide

For a high level overview of the Guides Infrastructure, take a look at this [blog post](https://micronaut.io/2021/04/12/improving-the-micronaut-guides-infrastructure/).

All the guides leverage [Micronaut Starter](https://github.com/micronaut-projects/micronaut-starter) core to create the projects. The idea is that one guide can generate up to six different projects, one per language (Java, Groovy and Kotlin) and build tool (Gradle and Maven).

### Guide structure

All the guides are in the `guides` directory in separate subdirectories. Inside the directory, the main file is `metadata.json` that describes the guide. All the fields are declared in [GuideMetadata](https://github.com/micronaut-projects/micronaut-guides/blob/master/buildSrc/src/main/groovy/io/micronaut/guides/GuideMetadata.groovy) class.

```json
{
  "title": "Micronaut HTTP Client",
  "intro": "Learn how to use Micronaut low-level HTTP Client. Simplify your code with the declarative HTTP client.",
  "authors": ["Sergio del Amo", "Iván López"],
  "tags": ["client", "rx", "flowable", "json-streams"],
  "category": "Getting Started",
  "publicationDate": "2018-07-02",
  "apps": [
    {
      "name": "default",
      "features": ["graalvm", "reactor"]
    }
  ]
}
```

Besides, the obvious fields that doesn't need any further explanation, the other are:

- `tags`: List of tags added to the guide. You don't need to include the language here because it is added automatically when generating the json file for the Guides webpage.
- `category`: Needs to be a valid value from the [Category](https://github.com/micronaut-projects/micronaut-guides/blob/master/buildSrc/src/main/java/io/micronaut/guides/Category.java) enum.
- `buildTools`: By default we generate the code in the guides for Gradle and Maven. If a guide is specific only for a build tool, define it here.
- `languages`: The guides should be written in the three languages. Sometimes we only write guides in one language or the guide only supports a specific language.
- `testFramework`: By default Java and Kotlin applications are tested with JUnit5 and Groovy applications with Spock. In some cases we have Java guides that are tested with Spock. Use this property to configure it.
- `skipGradleTests`: Set it to `true` to skip running the tests for the Gradle applications for the guide. This is useful when it's not easy to run tests on CI, for example for some cloud guides.
- `skipMavenTests`: Same as `skipGradleTests` but for Maven applications.
- `minimumJavaVersion`: If the guide needs a minimum Java version (for example JDK 17 for Records), define it in this property.
- `maximumJavaVersion`: If the guide needs a maximum Java version (for example JDK 11 for Azure Functions), define it in this property.
- `zipIncludes`: List of additional files to include in the generated zip file for the guide.
- `publish`: defaults to true for regular guides; set to false for partial/base guides
- `base`: defaults to null; if set, indicates directory name of the base guide to copy before copying the current
- `apps`: List of pairs `name`-`features` for the generated application. There are two types of guides, most of the guides only generate one application (single-app). In this case the name of the applications needs to be `default`. There are a few guides that generate multiple applications, so they need to be declared here:
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
The features need to be **valid** features from Starter because the list is used directly when generating the applications using Starter infrastructure. If you need a feature that is not available on Starter, create it in `buildSrc/src/main/java/io/micronaut/guides/feature`. Also declare the GAV coordinates and version in `buildSrc/src/main/resources/pom.xml`. Dependabot is configured in this project to look for that file and send pull requests to update the dependencies.

Inside the specific guide directory there should be a directory per language with the appropriate directory structure. All these files will be copied into the final guide directory after the guide is generated.

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

For multi-applications guides there needs to be an additional directory with the name of the application declared in `metadata.json` file:

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

There is only one Asciidoctor file per guide in the root directory of the guide (sibling to `metadata.json`). This unique file is used to generate all the combinations for the guide (language and build tool) so we need to take that into account when writing the guide. Name the Asciidoctor file the same as the directory, with an "adoc" extension, e.g. `micronaut-http-client.adoc` for the `micronaut-http-client` guide directory.

We don't really write a valid Asciidoctor file but our "own" Asciidoctor with custom kind-of-macros. Then during the build process we render the final HTML for the guide in two phases. In the first one we evaluate all of our custom macros and include and generate a new language-build tool version of the guide in `src/doc/asciidoc`. This directory is excluded from source control and needs to be considered temporary. Then we render the final HTML of the (up to) six guides from that generated and valid Asciidoctor file.

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

We have small pieces of text that are used in different guides. To avoid the duplication we have common snippets in the `src/docs/common` directory. For example the file `common-header-top.adoc`:

```asciidoc
= @guideTitle@

@guideIntro@

Authors: @authors@

Micronaut Version: @micronaut@
```

Will render the title, description, authors and version of all the guides. The variables defined between `@` signs will be evaluated and replaced during the first stage of the asciidoctor render. For example, for the Micronaut HTTP Client guide, the previous common snippet will generate:

```asciidoc
// Start: common-header-top.adoc
= Micronaut HTTP Client

Learn how to use Micronaut low-level HTTP Client. Simplify your code with the declarative HTTP client.

Authors: Sergio del Amo, Iván López

Micronaut Version: 3.2.7

// End: common-header-top.adoc
```

#### Custom macros

There are a number of custom macros available to make it easy writing a single asciidoctor file for all the guides and include the necessary source files, resources,... This is really important because when we include a source code snippet the base directory will change for every language the guide is written.

The following snippet from the HTTP Client guide:

```asciidoc
source:GithubConfiguration[]
```

Will generate the following Asciidoctor depending on the language of the guide:

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

There are also special custom blocks to exclude some code to be included in the generated guide based on some condition. This is useful when explaining something specific of the build tool (like how to run the tests with Gradle or Maven) or to exclude something depending on the language (for example do not render the GraalVM section in Groovy guides, as Groovy is not compatible with GraalVM).

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

To create a new guide use the following template as the base asciidoc file:

```asciidoc
common:header.adoc[]

common:requirements.adoc[]

common:completesolution.adoc[]

common:create-app.adoc[]

TODO: Describe the user step by step how to write the app. Use includes to reference real code: 

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

TODO Use the generic next step 

common:next.adoc[]

TODO or a personalised guide for the guide:

== Next steps

TODO: link to the documentation modules you used in the guide

```

### Testing the guide

When working on a new guide, generate it as explained before. The guide will be available in the `build/dist` directory and the applications will be in the `build/code` directory. You can open any directory in `build/code` directly in your IDE to make any changes but keep in mind copying the code back to the appropriate directory.

In the `build/code` directory a file `test.sh` is created to run all the tests for the guides generated. Run it locally to make sure it passes before submitting a new pull request.

You can run this test with a gradle task

```bash
./gradlew :____RunTestScript
```

where `____` is the camel-case name of your guide.  eg:

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

- Java CI: Run everytime we send a pull request or something is merged in `master`. The `test.sh` script explained before is executed.
- Java CI SNAPSHOT: There is a cronjob that runs daily the tests for the new Micronaut patch and minor versions to make sure everything will work when we release new versions in the future.
