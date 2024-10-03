package io.micronaut.guides.core

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.micronaut.context.ApplicationContext
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations

import java.nio.file.Path;
import java.time.LocalDate

class GuideProjectGeneratorTest {

    @TempDir
    Path tempDir;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private GuideGenerator guidesGenerator;

    @Mock
    private CategoryProvider categoryProvider;

    private GuideProjectGenerator guideProjectGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the ApplicationContext
        when(applicationContext.getBean(GuideGenerator.class)).thenReturn(guidesGenerator);

        // Mock the CategoryProvider
        Category mockCategory = mock(Category.class);
        when(mockCategory.getName()).thenReturn("Mock Category");
        when(categoryProvider.getAllCategories()).thenReturn(new Category[]{mockCategory});
        when(categoryProvider.findByName(anyString())).thenReturn(mockCategory);

        // Create GuideProjectGenerator with the mock CategoryProvider
        guideProjectGenerator = new GuideProjectGenerator("micronautguide", "example.micronaut", categoryProvider);
    }

    @Test
    void testParseGuideMetadata() {
        File dir = tempDir.resolve("test-guide").toFile();
        dir.mkdirs();
        String metadataConfigName = "metadata.json";

        // Create a mock JSON content
        String jsonContent = "{" +
                "\"title\": \"Test Guide\"," +
                "\"intro\": \"This is a test guide\"," +
                "\"authors\": [\"John Doe\"]," +
                "\"tags\": [\"test\"]," +
                "\"categories\": [\"CLOUD\"]," +
                "\"publicationDate\": \"2023-09-25\"," +
                "\"publish\": true" +
                "}";

        // Create a real file with the JSON content
        File configFile = new File(dir, metadataConfigName);
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(jsonContent);
        }

        // Use Mockito's spy to allow some real method calls
        File spyConfigFile = spy(configFile);

        // Call the method
        Optional<GuideMetadata> result = GuideProjectGenerator.parseGuideMetadata(dir, metadataConfigName);

        // Verify the result
        assertTrue(result.isPresent());
        GuideMetadata metadata = result.get();

        assertEquals("test-guide.adoc", metadata.getAsciidoctor());
        assertEquals("test-guide", metadata.getSlug());
        assertEquals("Test Guide", metadata.getTitle());
        assertEquals("This is a test guide", metadata.getIntro());
        assertEquals(Collections.singleton("John Doe"), metadata.getAuthors());
        assertEquals(Arrays.asList("test","mock-category"), metadata.getTags());
        assertEquals(LocalDate.parse("2023-09-25"), metadata.getPublicationDate());
        assertTrue(metadata.isPublish());
    }

    @Test
    void testGuidesOptions() {
        GuideMetadata metadata = new GuideMetadata();
        metadata.setBuildTools(Arrays.asList("gradle", "maven"));
        metadata.setLanguages(Arrays.asList("java", "kotlin"));
        metadata.setTestFramework("junit");
        metadata.skips = new HashSet<>();

        List<GuidesOption> options = GuideProjectGenerator.guidesOptions(metadata);

        assertEquals(4, options.size());
        assertTrue(options.stream().anyMatch(o -> o.getBuildTool() == BuildTool.GRADLE && o.getLanguage() == Language.JAVA));
        assertTrue(options.stream().anyMatch(o -> o.getBuildTool() == BuildTool.GRADLE && o.getLanguage() == Language.KOTLIN));
        assertTrue(options.stream().anyMatch(o -> o.getBuildTool() == BuildTool.MAVEN && o.getLanguage() == Language.JAVA));
        assertTrue(options.stream().anyMatch(o -> o.getBuildTool() == BuildTool.MAVEN && o.getLanguage() == Language.KOTLIN));
    }

    @Test
    void testDeleteEveryFileButSources(@TempDir Path tempDir) throws Exception {
        // Create some test files
        File javaFile = tempDir.resolve("Test.java").toFile();
        File groovyFile = tempDir.resolve("Test.groovy").toFile();
        File kotlinFile = tempDir.resolve("Test.kt").toFile();
        File textFile = tempDir.resolve("test.txt").toFile();

        javaFile.createNewFile();
        groovyFile.createNewFile();
        kotlinFile.createNewFile();
        textFile.createNewFile();

        GuideProjectGenerator.deleteEveryFileButSources(tempDir.toFile());

        assertTrue(javaFile.exists());
        assertTrue(groovyFile.exists());
        assertTrue(kotlinFile.exists());
        assertFalse(textFile.exists());
    }
}