package io.micronaut.guides.core;

import java.io.File;
import java.io.IOException;

public interface GuideProjectZipper {

    void zipDirectory(File folderToZip, File zipFile) throws IOException;
}
