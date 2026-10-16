package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.gradle.api.GradleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static io.micronaut.core.util.StringUtils.EMPTY_STRING;

@Singleton
public class DefaultFilesTransferUtility implements FilesTransferUtility {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFilesTransferUtility.class);
    private static final String EXTENSION_JAVA = ".java";
    private static final String EXTENSION_GROOVY = ".groovy";
    private static final String EXTENSION_KT = ".kt";
    private static final String EXTENSION_TOML = ".toml";
    /**
     * Shared resources in the root of {@code src/main/resources} or {@code src/test/resources} that only configure the JVM
     * (logging and bootstrap configuration) and must not be copied into a Python project.
     */
    private static final PathMatcher JVM_ONLY_RESOURCES = FileSystems.getDefault().getPathMatcher("glob:{logback*.xml,bootstrap*.*}");
    /**
     * Shared configuration formats that Micronaut loads next to {@code application.toml} and that are therefore not
     * copied into a Python project when it provides its own TOML file with the same name.
     */
    private static final Set<String> CONFIGURATION_EXTENSIONS = Set.of("properties", "yml", "yaml");

    private final LicenseLoader licenseLoader;
    private final GuidesConfiguration guidesConfiguration;

    DefaultFilesTransferUtility(LicenseLoader licenseLoader,
                                GuidesConfiguration guidesConfiguration) {
        this.licenseLoader = licenseLoader;
        this.guidesConfiguration = guidesConfiguration;
    }

    private static boolean fileContainsText(File file, String text) {
        try {
            return new String(Files.readAllBytes(file.toPath())).contains(text);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void copyGuideSourceFiles(File inputDir, Path destinationPath,
                                             String appName, String language,
                                             boolean ignoreMissingDirectories) throws IOException {

        // look for a common 'src' directory shared by multiple languages and copy those files first
        final String srcFolder = "src";
        Path srcPath = Paths.get(inputDir.getAbsolutePath(), appName, srcFolder);
        if (Files.exists(srcPath)) {
            Path resourcesPath = srcPath.resolve("main").resolve("resources");
            Path testResourcesPath = srcPath.resolve("test").resolve("resources");
            if (Language.PYTHON.toString().equals(language)) {
                Path pythonPath = Paths.get(inputDir.getAbsolutePath(), appName, Language.PYTHON.toString());
                copyPythonResources(resourcesPath, destinationPath.resolve("config"), pythonPath.resolve("config"),
                        destinationPath.resolve("src/main/resources"));
                copyPythonResources(testResourcesPath, destinationPath.resolve("tests-config"), pythonPath.resolve("tests-config"),
                        destinationPath.resolve("src/test/resources"));
                // directories are created lazily so that src/main and src/test are not left empty once their resources are skipped
                Files.walkFileTree(srcPath, new CopyFileVisitor(Paths.get(destinationPath.toString(), srcFolder), true) {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        if (dir.equals(resourcesPath) || dir.equals(testResourcesPath)) {
                            return FileVisitResult.SKIP_SUBTREE;
                        }
                        return super.preVisitDirectory(dir, attrs);
                    }
                });
            } else {
                Files.walkFileTree(srcPath, new CopyFileVisitor(Paths.get(destinationPath.toString(), srcFolder)));
            }
        }

        Path jvmPath = Paths.get(inputDir.getAbsolutePath(), appName, "jvm");
        if (!Language.PYTHON.toString().equals(language) && Files.exists(jvmPath)) {
            Files.walkFileTree(jvmPath, new CopyFileVisitor(destinationPath));
        }

        Path sourcePath = Paths.get(inputDir.getAbsolutePath(), appName, language);
        if (!Files.exists(sourcePath)) {
            sourcePath.toFile().mkdir();
        }
        if (Files.exists(sourcePath)) {
            // copy source/resource files for the current language
            Files.walkFileTree(sourcePath, new CopyFileVisitor(destinationPath));
        } else if (!ignoreMissingDirectories) {
            throw new GradleException("source directory " + sourcePath.toFile().getAbsolutePath() + " does not exist");
        }
    }

    private static void copyPythonResources(Path resourcesPath, Path configPath, Path pythonConfigPath,
                                            Path destinationResourcesPath) throws IOException {
        if (!Files.exists(resourcesPath)) {
            return;
        }
        Files.walkFileTree(resourcesPath, new CopyFileVisitor(configPath, true) {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (isJvmOnlyResource(resourcesPath.relativize(file), pythonConfigPath)) {
                    return FileVisitResult.CONTINUE;
                }
                return super.visitFile(file, attrs);
            }
        });
        Files.walkFileTree(resourcesPath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(destinationResourcesPath.resolve(resourcesPath.relativize(file)));
                return FileVisitResult.CONTINUE;
            }
        });
        // deleting the stale copies must not leave empty src/main/resources and src/main (or src/test...) directories behind
        deleteEmptyDirectories(destinationResourcesPath);
        deleteIfEmpty(destinationResourcesPath.getParent());
    }

    /**
     * @param resource the path of a shared resource, relative to {@code src/main/resources} or {@code src/test/resources}
     * @param pythonConfigPath the guide's {@code python/config} or {@code python/tests-config} directory
     * @return whether the shared resource configures the JVM only: root level logging and bootstrap configuration, or
     * configuration for which the guide provides a TOML file with the same name for Python
     */
    private static boolean isJvmOnlyResource(Path resource, Path pythonConfigPath) {
        Path fileName = resource.getFileName();
        if (resource.getNameCount() == 1 && JVM_ONLY_RESOURCES.matches(fileName)) {
            return true;
        }
        String name = fileName.toString();
        int extensionIndex = name.lastIndexOf('.');
        if (extensionIndex <= 0 || !CONFIGURATION_EXTENSIONS.contains(name.substring(extensionIndex + 1))) {
            return false;
        }
        return Files.exists(pythonConfigPath.resolve(resource.resolveSibling(name.substring(0, extensionIndex) + EXTENSION_TOML)));
    }

    private static void deleteEmptyDirectories(Path directory) throws IOException {
        if (!Files.isDirectory(directory)) {
            return;
        }
        Files.walkFileTree(directory, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                deleteIfEmpty(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void deleteIfEmpty(Path directory) throws IOException {
        if (!Files.isDirectory(directory)) {
            return;
        }
        try (var entries = Files.list(directory)) {
            if (entries.findAny().isEmpty()) {
                Files.delete(directory);
            }
        }
    }

    private static File fileToDelete(File destination, String path) {
        return Paths.get(destination.getAbsolutePath(), path).toFile();
    }

    private static void copyFile(File inputDir, File destinationRoot, String filePath) throws IOException {
        File sourceFile = new File(inputDir, filePath);
        File destinationFile = new File(destinationRoot, filePath);

        File destinationFileDir = destinationFile.getParentFile();
        if (!destinationFileDir.exists()) {
            Files.createDirectories(destinationFileDir.toPath());
        }

        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void transferFiles(@NotNull @NonNull File inputDirectory, @NotNull @NonNull File outputDirectory, @NotNull @NonNull Guide guide) throws IOException {
        List<GuidesOption> guidesOptionList = GuideGenerationUtils.guidesOptions(guide, LOG);
        for (GuidesOption guidesOption : guidesOptionList) {
            for (App app : guide.apps()) {
                String appName = app.name().equals(guidesConfiguration.getDefaultAppName()) ? EMPTY_STRING : app.name();
                String folder = MacroUtils.getSourceDir(guide.slug(), guidesOption);
                Path destinationPath = Paths.get(outputDirectory.getAbsolutePath(), folder, appName);
                File destination = destinationPath.toFile();

                if (Language.PYTHON.toString().equals(guidesOption.getLanguage().toString())) {
                    movePythonResources(destinationPath);
                }

                if (guide.base() != null) {
                    File baseDir = new File(inputDirectory.getParentFile(), guide.base());
                    copyGuideSourceFiles(baseDir, destinationPath, appName, guidesOption.getLanguage().toString(), true);
                }

                copyGuideSourceFiles(inputDirectory, destinationPath, appName, guidesOption.getLanguage().toString(), false);

                if (app.excludeSource() != null) {
                    for (String mainSource : app.excludeSource()) {
                        File f = fileToDelete(destination, GuideGenerationUtils.mainPath(appName, mainSource, guidesOption, guidesConfiguration));
                        if (f.exists()) {
                            f.delete();
                        }
                        f = fileToDelete(destination, GuideGenerationUtils.mainPath(appName, mainSource, guidesOption, guidesConfiguration));
                        if (f.exists()) {
                            f.delete();
                        }
                    }
                }

                if (app.excludeTest() != null) {
                    for (String testSource : app.excludeTest()) {
                        File f = fileToDelete(destination, GuideGenerationUtils.testPath(appName, testSource, guidesOption, guidesConfiguration));
                        if (f.exists()) {
                            f.delete();
                        }
                        f = fileToDelete(destination, GuideGenerationUtils.testPath(appName, testSource, guidesOption, guidesConfiguration));
                        if (f.exists()) {
                            f.delete();
                        }
                    }
                }

                if (guide.zipIncludes() != null) {
                    File destinationRoot = new File(outputDirectory.getAbsolutePath(), folder);
                    for (String zipInclude : guide.zipIncludes()) {
                        copyFile(inputDirectory, destinationRoot, zipInclude);
                    }
                }
                addLicenses(new File(outputDirectory.getAbsolutePath(), folder));
            }
        }
    }

    private static void movePythonResources(Path destinationPath) throws IOException {
        movePythonResourceFolder(destinationPath, "views");
        movePythonResourceFolder(destinationPath, "static");
    }

    private static void movePythonResourceFolder(Path destinationPath, String resourceFolder) throws IOException {
        Path sourcePath = destinationPath.resolve("src").resolve("main").resolve("resources").resolve(resourceFolder);
        if (!Files.exists(sourcePath)) {
            return;
        }

        Path targetPath = destinationPath.resolve("config").resolve(resourceFolder);
        Files.createDirectories(targetPath);
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.move(file, targetPath.resolve(sourcePath.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    void addLicenses(File folder) {
        String licenseHeader = licenseLoader.getLicenseHeaderText();
        Arrays.stream(folder.listFiles()).forEach(file -> {
            if ((file.getPath().endsWith(EXTENSION_JAVA) || file.getPath().endsWith(EXTENSION_GROOVY) || file.getPath().endsWith(EXTENSION_KT))
                    && !fileContainsText(file, "Licensed under")) {
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    Files.write(file.toPath(), (licenseHeader + content).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
