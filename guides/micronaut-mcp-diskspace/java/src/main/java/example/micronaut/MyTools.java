package example.micronaut;

import io.micronaut.mcp.annotations.Tool;
import jakarta.inject.Singleton;
import java.io.File;

@Singleton // <1>
class MyTools {
    @Tool(title = "Free Disk Space",
          description = "Return the free disk space in the users computer")  // <2>
    String freeDiskSpace() {
        return DiskUtils.freeDiskSpace();
    }
}
