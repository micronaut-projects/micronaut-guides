package example.micronaut;

import io.micronaut.mcp.annotations.Tool;
import jakarta.inject.Singleton;
import java.io.File;

@Singleton
class MyTools {
    @Tool(title = "Free Disk Space",
            description = "Return the free disk space in the users computer")
    String freeDiskSpace() {
        return DiskUtils.freeDiskSpace();
    }
}
