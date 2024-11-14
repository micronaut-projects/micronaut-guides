package io.micronaut.guides.core.asciidoc;

import io.micronaut.guides.core.*;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class AsciidocConverterTest {

    @Inject
    AsciidocConverter asciidocConverter;

    @Inject
    GuideParser guideParser;

    @Inject
    GuideProjectGenerator guideProjectGenerator;

    @Inject
    FilesTransferUtility filesTransferUtility;

    @Test
    void testConvert() throws IOException {
        String outputPath = "build/tmp/test/adding-commit-info";
        File outputDirectory = new File(outputPath);
        outputDirectory.mkdir();

        String path = "src/test/resources/other-guides/adding-commit-info";
        File file = new File(path);
        Guide guide = guideParser.parseGuideMetadata(file, "metadata.json").orElseThrow();

        guideProjectGenerator.generate(outputDirectory, guide);

        filesTransferUtility.transferFiles(file, outputDirectory, guide);

        File sourceFile = new File("src/test/resources/adding-commit-info-gradle-java.adoc");
        File destinationFile = new File("build/tmp/test/docs/adding-commit-info-gradle-java.html");
        asciidocConverter.convert(sourceFile, destinationFile);
        String expected = TestUtils.readFile(new File("src/test/resources/adding-commit-info-gradle-java-expected.html"));
        String result = TestUtils.readFile(destinationFile);
        assertEquals(expected, result);
    }

    @Test
    void testConvertIncludeAdoc() {
        File sourceFile = new File("src/test/resources/other-guides/adding-commit-info/adding-commit-info.adoc");
        File destinationFile = new File("build/tmp/test/docs/adding-commit-info.html");
        asciidocConverter.convert(sourceFile, destinationFile);
        File expectedFile = new File("src/test/resources/adding-commit-info-expected.html");
        String expected = TestUtils.readFile(expectedFile);
        String result = TestUtils.readFile(destinationFile);
        assertEquals(expected, result);
    }

    @Test
    void testConvertRawHtml() {
        File sourceFile = new File("src/test/resources/other-guides/adding-commit-info/adding-commit-info.adoc");
        String result = asciidocConverter.convert(sourceFile);
        String expected = """
                <div id="toc" class="toc">
                <div id="toctitle">Table of Contents</div>
                <ul class="sectlevel1">
                <li><a href="#initialize-git-repository">1. Initialize Git Repository</a></li>
                <li><a href="#management">2. Management</a></li>
                </ul>
                </div>
                <div id="preamble">
                <div class="sectionbody">
                <div class="paragraph">
                <p>@guideIntro@</p>
                </div>
                <div class="paragraph">
                <p>Authors: @authors@</p>
                </div>
                <div class="paragraph">
                <p>Micronaut Version: @micronaut@</p>
                </div>
                <div class="paragraph">
                <p>Execute the following command to run a MySQL container:</p>
                </div>
                <div class="listingblock">
                <div class="content">
                <pre class="CodeRay highlight"><code data-lang="bash">docker run -it --rm \\
                    -p 3306:3306 \\
                    -e MYSQL_DATABASE=db \\
                    -e MYSQL_USER=sherlock \\
                    -e MYSQL_PASSWORD=elementary \\
                    -e MYSQL_ALLOW_EMPTY_PASSWORD=true \\
                    mysql:8</code></pre>
                </div>
                </div>
                <div class="admonitionblock tip">
                <table>
                <tr>
                <td class="icon">
                <i class="fa icon-tip" title="Tip"></i>
                </td>
                <td class="content">
                If you are using macOS on Apple Silicon – e.g. M1, M1 Pro, etc. – Docker might fail to pull an image for <code>mysql:8</code>. In that case substitute <code>mysql:oracle</code>.
                </td>
                </tr>
                </table>
                </div>
                <div class="paragraph">
                <p>Export users microservice image repository to the <code>USERS_REPOSITORY</code> environment variable.</p>
                </div>
                <div class="listingblock">
                <div class="content">
                <pre class="CodeRay highlight"><code data-lang="bash">export USERS_REPOSITORY=&quot;gcr.io/$GCP_PROJECT_ID/users&quot;</code></pre>
                </div>
                </div>
                <div class="colist arabic">
                <table>
                <tr>
                <td><i class="conum" data-value="1"></i><b>1</b></td>
                <td>The class is defined as a controller with the @api@/io/micronaut/http/annotation/Controller.html[@Controller] annotation mapped to the path <code>dir/path/example.txt</code>.</td>
                </tr>
                <tr>
                <td><i class="conum" data-value="2"></i><b>2</b></td>
                <td>The class is defined as a controller with the @api@/io/micronaut/http/annotation/Controller.html[@Controller] annotation mapped to the path <code>dir/path/example.txt</code>.</td>
                </tr>
                <tr>
                <td><i class="conum" data-value="3"></i><b>3</b></td>
                <td>The class is defined as a controller with the @api@/io/micronaut/http/annotation/Controller.html[@Controller] annotation mapped to the path <code>dir/path/example.txt</code>.</td>
                </tr>
                </table>
                </div>
                <div class="paragraph">
                <p>Test</p>
                </div>
                <div class="paragraph">
                <p>In this guide, we will add git commit info to your Micronaut build artifacts and running application.
                There are many benefits of keeping your commit info handy:</p>
                </div>
                <div class="ulist">
                <ul>
                <li>
                <p>Commit info is encapsulated within the built artifacts</p>
                </li>
                <li>
                <p>Fast authoritative means of identifying what specific code is running in an environment</p>
                </li>
                <li>
                <p>This solution doesn&#8217;t rely on external tracking mechanisms</p>
                </li>
                <li>
                <p>Transparency and reproducibility when investigating issues</p>
                </li>
                </ul>
                </div>
                <div class="admonitionblock note">
                <table>
                <tr>
                <td class="icon">
                <i class="fa icon-note" title="Note"></i>
                </td>
                <td class="content">
                Before running the downloaded project, follow the steps described in the <strong>Initialize Git Repository</strong> section below.
                </td>
                </tr>
                </table>
                </div>
                </div>
                </div>
                <div class="sect1">
                <h2 id="initialize-git-repository">1. Initialize Git Repository</h2>
                <div class="sectionbody">
                <div class="paragraph">
                <p>The project aims to demonstrate how to provide Git commit information to the
                <code>/info</code> endpoint and in order for that to work the project needs to be in a Git repository.
                After creating the project, initialize a Git repository from the root of the newly created project:</p>
                </div>
                <div class="listingblock">
                <div class="content">
                <pre class="CodeRay highlight"><code data-lang="bash">cd micronautguide
                git init
                git add .
                git commit -am &quot;Initial project&quot;</code></pre>
                </div>
                </div>
                </div>
                </div>
                <div class="sect1">
                <h2 id="management">2. Management</h2>
                <div class="sectionbody">
                <div class="paragraph">
                <p>Inspired by Spring Boot and Grails, the Micronaut management dependency adds support for monitoring of your application via endpoints: special URIs that return details about the health and state of your application.</p>
                </div>
                <div class="paragraph">
                <p>To use the management features described in this section, add the dependency on your classpath.</p>
                </div>
                </div>
                </div>""";
        assertEquals(expected, result);
    }
}
