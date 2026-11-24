package io.micronaut.guides.core;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class CopyFileVisitor extends SimpleFileVisitor<Path> {

    private final Path targetPath;
    private final boolean createDirectoriesLazily;
    private Path sourcePath;

    public CopyFileVisitor(Path targetPath) {
        this(targetPath, false);
    }

    /**
     * @param targetPath the directory the walked tree is copied into
     * @param createDirectoriesLazily when {@code true}, destination directories are only created once a file is
     *                                copied into them, so source directories that end up with nothing copied
     *                                (for example because their only content is skipped) leave no empty
     *                                directory behind
     */
    public CopyFileVisitor(Path targetPath, boolean createDirectoriesLazily) {
        this.targetPath = targetPath;
        this.createDirectoriesLazily = createDirectoriesLazily;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path dir,
                                             final BasicFileAttributes attrs) throws IOException {
        if (sourcePath == null) {
            sourcePath = dir;
        } else if (!createDirectoriesLazily) {
            Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(final Path file,
                                     final BasicFileAttributes attrs) throws IOException {
        Path target = targetPath.resolve(sourcePath.relativize(file));
        if (createDirectoriesLazily) {
            Files.createDirectories(target.getParent());
        }
        Files.copy(file, target, REPLACE_EXISTING);
        return CONTINUE;
    }
}
