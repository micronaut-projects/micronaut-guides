package io.micronaut.guides.feature.springboot.replacements;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.feature.springboot.SpringBootApplicationFeature;
import io.micronaut.guides.feature.springboot.template.springBootGitignore;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.maven.MavenBuild;
import io.micronaut.starter.build.maven.MavenBuildCreator;
import io.micronaut.starter.build.maven.MavenRepository;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.gitignore;
import io.micronaut.starter.feature.build.maven.Maven;
import io.micronaut.starter.feature.build.maven.templates.multimodule;
import io.micronaut.starter.feature.build.maven.templates.pom;
import io.micronaut.guides.feature.springboot.template.springBootGitignore;
import io.micronaut.guides.feature.springboot.template.springBootGenericPom;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.URLTemplate;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static io.micronaut.starter.build.Repository.micronautRepositories;

@Replaces(Maven.class)
@Singleton
public class MavenReplacement extends Maven {
    protected static final String WRAPPER_JAR = ".mvn/wrapper/maven-wrapper.jar";
    protected static final String WRAPPER_PROPS = ".mvn/wrapper/maven-wrapper.properties";
    protected static final String WRAPPER_DOWNLOADER = ".mvn/wrapper/MavenWrapperDownloader.java";
    protected static final String MAVEN_PREFIX = "maven/";

    protected final MavenBuildCreator dependencyResolver;
    protected final CoordinateResolver coordinateResolver;

    public MavenReplacement(MavenBuildCreator dependencyResolver, CoordinateResolver coordinateResolver) {
        super(dependencyResolver);
        this.dependencyResolver = dependencyResolver;
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    @NonNull
    public String getName() {
        return "maven";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addMavenWrapper(generatorContext);
        addPom(generatorContext);
        addGitIgnore(generatorContext);
        Collection<String> moduleNames = generatorContext.getModuleNames();
        if (moduleNames.size() > 1) {
            List<MavenRepository> mavenRepositories = VersionInfo.getMicronautVersion().endsWith("-SNAPSHOT") ?
                    MavenRepository.listOf(micronautRepositories()) :
                    null;
            generatorContext.addTemplate("multi-module-pom", new RockerTemplate(Template.ROOT, generatorContext.getBuildTool().getBuildFileName(), multimodule.template(mavenRepositories, generatorContext.getProject(), moduleNames)));
        }
    }

    protected void addGitIgnore(GeneratorContext generatorContext) {
        generatorContext.addTemplate("gitignore", new RockerTemplate(Template.ROOT, ".gitignore", gitIgnore(generatorContext)));
    }

    protected RockerModel gitIgnore(GeneratorContext generatorContext) {
        if (SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
            return springBootGitignore.template();
        }
        return gitignore.template();
    }

    protected void addPom(GeneratorContext generatorContext) {
        MavenBuild mavenBuild = createBuild(generatorContext);
        generatorContext.addTemplate("mavenPom", new RockerTemplate("pom.xml", pom(generatorContext, mavenBuild)));
    }

    protected RockerModel pom(GeneratorContext generatorContext, MavenBuild mavenBuild) {
        if (SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
            Coordinate springBootParent = coordinateResolver.resolve("spring-boot-starter-parent")
                    .orElseGet(() -> {
                        throw new NoSuchElementException("No value present");
                    });

            String sourceDirectory = generatorContext.getLanguage() == Language.KOTLIN ? "${project.basedir}/src/main/kotlin" : null;
            String testSourceDirectory = generatorContext.getLanguage() == Language.KOTLIN ? "${project.basedir}/src/test/kotlin" : null;
            return springBootGenericPom.template(generatorContext.getProject(),
                    mavenBuild,
                    springBootParent.getGroupId(),
                    springBootParent.getArtifactId(),
                    springBootParent.getVersion(),
                    true,
                    sourceDirectory,
                    testSourceDirectory);
        }
        return pom.template(
                generatorContext.getApplicationType(),
                generatorContext.getProject(),
                generatorContext.getFeatures(),
                mavenBuild
        );
    }

    protected MavenBuild createBuild(GeneratorContext generatorContext) {
        return dependencyResolver.create(generatorContext);
    }

    protected void addMavenWrapper(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate("mavenWrapperJar", new BinaryTemplate(Template.ROOT, WRAPPER_JAR, classLoader.getResource(MAVEN_PREFIX + WRAPPER_JAR)));
        generatorContext.addTemplate("mavenWrapperProperties", new URLTemplate(Template.ROOT, WRAPPER_PROPS, classLoader.getResource(MAVEN_PREFIX + WRAPPER_PROPS)));
        generatorContext.addTemplate("mavenWrapperDownloader", new URLTemplate(Template.ROOT, WRAPPER_DOWNLOADER, classLoader.getResource(MAVEN_PREFIX + WRAPPER_DOWNLOADER)));
        generatorContext.addTemplate("mavenWrapper", new URLTemplate(Template.ROOT, "mvnw", classLoader.getResource(MAVEN_PREFIX + "mvnw"), true));
        generatorContext.addTemplate("mavenWrapperBat", new URLTemplate(Template.ROOT, "mvnw.bat", classLoader.getResource(MAVEN_PREFIX + "mvnw.cmd"), false));

    }

    @Override
    public boolean shouldApply(ApplicationType applicationType,
                               Options options,
                               Set<Feature> selectedFeatures) {

        return options.getBuildTool() == BuildTool.MAVEN;
    }
}
