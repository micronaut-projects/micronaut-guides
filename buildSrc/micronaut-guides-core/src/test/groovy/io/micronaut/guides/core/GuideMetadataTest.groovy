package io.micronaut.guides.core

import io.micronaut.guides.core.GuideMetadata
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class GuideMetadataTest {

    private GuideMetadata guideMetadata;

    @BeforeEach
    void setUp() {
        guideMetadata = new GuideMetadata();
        guideMetadata.setSlug("test-guide");
        guideMetadata.setTitle("Test Guide");
        guideMetadata.setAuthors(new HashSet<>(Arrays.asList("Author1", "Author2")));
        guideMetadata.setTags(Arrays.asList("tag1", "tag2"));
        List<Category> categories = new LinkedList<Category>();
        categories.add(TestCategory.TEST_CATEGORY)
        guideMetadata.setCategories(categories);
        guideMetadata.setPublicationDate(LocalDate.now());

        GuideMetadata.App app = new GuideMetadata.App();
        app.setName("TestApp");
        app.setApplicationType(ApplicationType.DEFAULT);
        app.setVisibleFeatures(Arrays.asList("graalvm", "micronaut-validation"));
        app.setJavaFeatures(Arrays.asList("java-feature"));
        app.setKotlinFeatures(Arrays.asList("kotlin-feature"));
        app.setGroovyFeatures(Arrays.asList("groovy-feature"));

        guideMetadata.setApps(Arrays.asList(app));
    }

    @Test
    void testGetTags() {
        List<String> tags = guideMetadata.getTags();
        System.out.println(tags);
        assertTrue(tags.containsAll(Arrays.asList("tag1", "tag2","graalvm","java-feature","kotlin-feature","groovy-feature","test-category","validation")));
        assertEquals(8, tags.size());
    }

    @Test
    void testShouldSkip() {
        guideMetadata.setSkipGradleTests(true);
        guideMetadata.setSkipMavenTests(false);

        assertTrue(guideMetadata.shouldSkip(BuildTool.GRADLE));
        assertFalse(guideMetadata.shouldSkip(BuildTool.MAVEN));
        assertFalse(guideMetadata.shouldSkip(BuildTool.GRADLE_KOTLIN));
    }

    @Test
    void testShouldSkipWithLanguage() {
        GuideMetadata.Skip skip = new GuideMetadata.Skip(BuildTool.GRADLE, Language.JAVA);
        guideMetadata.setSkips(new HashSet<>(Arrays.asList(skip)));

        assertTrue(guideMetadata.shouldSkip(BuildTool.GRADLE, Language.JAVA));
        assertFalse(guideMetadata.shouldSkip(BuildTool.MAVEN, Language.JAVA));
        assertFalse(guideMetadata.shouldSkip(BuildTool.GRADLE, Language.KOTLIN));
    }

    @Test
    void testAppGetFeatures() {
        GuideMetadata.App app = guideMetadata.getApps().get(0);

        List<String> javaFeatures = app.getFeatures(Language.JAVA);
        assertTrue(javaFeatures.containsAll(Arrays.asList("java-feature")));

        List<String> kotlinFeatures = app.getFeatures(Language.KOTLIN);
        assertTrue(kotlinFeatures.containsAll(Arrays.asList("kotlin-feature")));

        List<String> groovyFeatures = app.getFeatures(Language.GROOVY);
        assertTrue(groovyFeatures.containsAll(Arrays.asList("groovy-feature")));
    }

    @Test
    void testAppGetVisibleFeatures() {
        GuideMetadata.App app = guideMetadata.getApps().get(0);

        List<String> javaVisibleFeatures = app.getVisibleFeatures(Language.JAVA);
        assertEquals(3, javaVisibleFeatures.size());
        assertTrue(javaVisibleFeatures.containsAll(Arrays.asList("graalvm", "micronaut-validation", "java-feature")));

        List<String> kotlinVisibleFeatures = app.getVisibleFeatures(Language.KOTLIN);
        assertEquals(3, kotlinVisibleFeatures.size());
        assertTrue(kotlinVisibleFeatures.containsAll(Arrays.asList("graalvm", "micronaut-validation", "kotlin-feature")));

        List<String> groovyVisibleFeatures = app.getVisibleFeatures(Language.GROOVY);
        assertEquals(3, groovyVisibleFeatures.size());
        assertTrue(groovyVisibleFeatures.containsAll(Arrays.asList("graalvm", "micronaut-validation", "groovy-feature")));
    }
}