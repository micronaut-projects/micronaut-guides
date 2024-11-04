package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;
import org.gradle.api.GradleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import static io.micronaut.core.util.StringUtils.EMPTY_STRING;

public class DefaultFilesCopyUtility implements FilesCopyUtility {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFilesCopyUtility.class);
    private static final String EXTENSION_JAVA = ".java";
    private static final String EXTENSION_GROOVY = ".groovy";
    private static final String EXTENSION_KT = ".kt";

    private LicenseLoader licenseLoader;
    private GuidesConfiguration guidesConfiguration;

    DefaultFilesCopyUtility(LicenseLoader licenseLoader,
                            GuidesConfiguration guidesConfiguration){
        this.licenseLoader = licenseLoader;
        this.guidesConfiguration = guidesConfiguration;
    }

    @Override
    public void copyFiles(@NotNull @NonNull File outputDirectory, @NotNull @NonNull File inputDirectory, @NotNull @NonNull Guide guide) throws IOException {
        List<GuidesOption> guidesOptionList = GuideGenerationUtils.guidesOptions(guide,LOG);
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
        if (Files.exists(srcPath)) {
            Files.walkFileTree(srcPath, new CopyFileVisitor(Paths.get(destinationPath.toString(), srcFolder)));
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
}
