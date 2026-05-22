package io.micronaut.guides;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.util.NameUtils;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.starter.options.BuildTool.GRADLE;
import static io.micronaut.starter.options.JdkVersion.JDK_25;

@Singleton
public class GuidesGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(GuidesGenerator.class);
    private static final String ORACLE_CLOUD_ATP = "oracle-cloud-atp";
    private static final String MICRONAUT_MAVEN_GROUP = "io.micronaut.maven";
    private static final String MICRONAUT_MAVEN_PLUGIN = "micronaut-maven-plugin";
    private static final String TEST_RESOURCES_GROUP = "io.micronaut.testresources";
    private static final String ORACLE_FREE_TEST_RESOURCES = "micronaut-test-resources-jdbc-oracle-free";

    private final ProjectGenerator projectGenerator;

    public GuidesGenerator(ProjectGenerator projectGenerator) {
        this.projectGenerator = projectGenerator;
    }

    public void generateAppIntoDirectory(
            @NonNull File directory,
            @NotNull ApplicationType type,
            @NotNull String packageAndName,
            @Nullable String framework,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion) throws IOException {
        GeneratorContext generatorContext = createProjectGeneratorContext(type, packageAndName, framework, features, buildTool, testFramework, lang, javaVersion);
        try {
            projectGenerator.generate(type,
                    generatorContext.getProject(),
                    new FileSystemOutputHandler(directory, ConsoleOutput.NOOP),
                    generatorContext);
            configureOracleFreeTestResourcesIfNeeded(directory, features, buildTool);
        } catch (Exception e) {
            LOG.error("Error generating application: " + e.getMessage(), e);
            throw new IOException(e.getMessage(), e);
        }
    }

    private static void configureOracleFreeTestResourcesIfNeeded(
            File directory,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool) throws Exception {
        if (buildTool != BuildTool.MAVEN || features == null || !features.contains(ORACLE_CLOUD_ATP)) {
            return;
        }

        File pom = new File(directory, "pom.xml");
        if (!pom.isFile()) {
            return;
        }

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        Document document = documentBuilderFactory.newDocumentBuilder().parse(pom);
        Element project = document.getDocumentElement();
        Element micronautMavenPlugin = findMicronautMavenPlugin(project);
        if (micronautMavenPlugin == null) {
            return;
        }

        Element configuration = getOrCreateChild(document, micronautMavenPlugin, "configuration");
        Element testResourcesDependencies = getOrCreateChild(document, configuration, "testResourcesDependencies");
        if (containsDependency(testResourcesDependencies, TEST_RESOURCES_GROUP, ORACLE_FREE_TEST_RESOURCES)) {
            return;
        }

        Element dependency = document.createElement("dependency");
        appendElement(document, dependency, "groupId", TEST_RESOURCES_GROUP);
        appendElement(document, dependency, "artifactId", ORACLE_FREE_TEST_RESOURCES);
        testResourcesDependencies.appendChild(dependency);

        removeWhitespaceNodes(project);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        var transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(pom));
    }

    private static Element findMicronautMavenPlugin(Element project) {
        NodeList pluginNodes = project.getElementsByTagName("plugin");
        for (int i = 0; i < pluginNodes.getLength(); i++) {
            Element plugin = (Element) pluginNodes.item(i);
            if (MICRONAUT_MAVEN_GROUP.equals(childText(plugin, "groupId"))
                    && MICRONAUT_MAVEN_PLUGIN.equals(childText(plugin, "artifactId"))) {
                return plugin;
            }
        }
        return null;
    }

    private static Element getOrCreateChild(Document document, Element parent, String tagName) {
        Element existing = childElement(parent, tagName);
        if (existing != null) {
            return existing;
        }
        Element child = document.createElement(tagName);
        parent.appendChild(child);
        return child;
    }

    private static boolean containsDependency(Element dependencies, String groupId, String artifactId) {
        NodeList dependencyNodes = dependencies.getElementsByTagName("dependency");
        for (int i = 0; i < dependencyNodes.getLength(); i++) {
            Element dependency = (Element) dependencyNodes.item(i);
            if (groupId.equals(childText(dependency, "groupId"))
                    && artifactId.equals(childText(dependency, "artifactId"))) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private static Element childElement(Element element, String tagName) {
        NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element child && tagName.equals(child.getTagName())) {
                return child;
            }
        }
        return null;
    }

    private static String childText(Element element, String tagName) {
        Element child = childElement(element, tagName);
        return child == null ? null : child.getTextContent();
    }

    private static void appendElement(Document document, Element parent, String tagName, String textContent) {
        Element element = document.createElement(tagName);
        element.setTextContent(textContent);
        parent.appendChild(element);
    }

    private static void removeWhitespaceNodes(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = childNodes.getLength() - 1; i >= 0; i--) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getTextContent().isBlank()) {
                node.removeChild(child);
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeWhitespaceNodes(child);
            }
        }
    }

    GeneratorContext createProjectGeneratorContext(
            ApplicationType type,
            @Pattern(regexp = "[\\w\\d-_\\.]+") String packageAndName,
            @Nullable String framework,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion) throws IllegalArgumentException {
        Project project;
        try {
            project = NameUtils.parse(packageAndName);
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(BAD_REQUEST, "Invalid project name: " + e.getMessage());
        }

        return projectGenerator.createGeneratorContext(
                type,
                project,
                new Options(
                        lang,
                        testFramework != null ? testFramework.toTestFramework() : null,
                        buildTool == null ? GRADLE : buildTool,
                        javaVersion != null ? javaVersion : JDK_25).withFramework(framework),
                null,
                features != null ? features : Collections.emptyList(),
                ConsoleOutput.NOOP
        );
    }
}
