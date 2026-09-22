package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.gradle.api.GradleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static io.micronaut.core.util.StringUtils.EMPTY_STRING;
import static io.micronaut.starter.options.Language.PYTHON;

@Singleton
public class DefaultFilesTransferUtility implements FilesTransferUtility {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFilesTransferUtility.class);
    private static final String EXTENSION_JAVA = ".java";
    private static final String EXTENSION_GROOVY = ".groovy";
    private static final String EXTENSION_KT = ".kt";

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

    private static void copyGuideSourceFiles(File inputDir, Path destinationPath,
                                             String appName, String language,
                                             boolean ignoreMissingDirectories) throws IOException {

        // look for a common 'src' directory shared by multiple languages and copy those files first
        final String srcFolder = "src";
        Path srcPath = Paths.get(inputDir.getAbsolutePath(), appName, srcFolder);
        Path sourcePath = Paths.get(inputDir.getAbsolutePath(), appName, language);
        if (Files.exists(srcPath)) {
            if (language.equals(PYTHON.toString())) {
                copySharedPythonResources(srcPath, sourcePath, destinationPath);
            } else {
                Files.walkFileTree(srcPath, new CopyFileVisitor(Paths.get(destinationPath.toString(), srcFolder)));
            }
        }

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

    private static void copySharedPythonResources(Path srcPath, Path sourcePath, Path destinationPath) throws IOException {
        Path pythonConfigPath = sourcePath.resolve("config");
        copySharedPythonResourceDirectory(srcPath.resolve("main/resources"), destinationPath.resolve("config"), pythonConfigPath);
        Path pythonTestConfigPath = sourcePath.resolve("tests-config");
        copySharedPythonResourceDirectory(srcPath.resolve("test/resources"), destinationPath.resolve("tests-config"), pythonTestConfigPath);
        copySharedPythonResourceDirectory(srcPath.resolve("test-resources"), destinationPath.resolve("tests-config"), pythonTestConfigPath);
    }

    private static void copySharedPythonResourceDirectory(Path resourcePath, Path destinationPath, Path pythonSpecificPath) throws IOException {
        if (!Files.exists(resourcePath)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(resourcePath)) {
            paths.filter(Files::isRegularFile).forEach(source -> {
                try {
                    Path relative = resourcePath.relativize(source);
                    if (isOverriddenByPythonConfig(relative, pythonSpecificPath)) {
                        return;
                    }
                    Path destination = destinationPath.resolve(relative);
                    Files.createDirectories(destination.getParent());
                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    private static boolean isOverriddenByPythonConfig(Path relative, Path pythonSpecificPath) throws IOException {
        if (Files.exists(pythonSpecificPath.resolve(relative))) {
            return true;
        }
        if (relative.getNameCount() != 1 || !Files.isDirectory(pythonSpecificPath)) {
            return false;
        }
        String filename = relative.getFileName().toString();
        int extensionIndex = filename.lastIndexOf('.');
        if (extensionIndex < 1) {
            return false;
        }
        String basename = filename.substring(0, extensionIndex);
        if (!isEnvironmentConfigBasename(basename)) {
            return false;
        }
        try (Stream<Path> paths = Files.list(pythonSpecificPath)) {
            return paths.filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .anyMatch(name -> name.startsWith(basename + "."));
        }
    }

    private static boolean isEnvironmentConfigBasename(String basename) {
        return basename.equals("application")
                || basename.startsWith("application-")
                || basename.equals("bootstrap")
                || basename.startsWith("bootstrap-");
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
                        if (guidesOption.getBuildTool() == io.micronaut.starter.options.BuildTool.PYRONAUT && guidesOption.getLanguage() == PYTHON) {
                            f = new File(destination, "tests/" + MacroUtils.pythonTestModuleName(testSource) + "." + guidesOption.getLanguage().getExtension());
                            if (f.exists()) {
                                f.delete();
                            }
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
