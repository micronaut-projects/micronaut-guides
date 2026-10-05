from jakarta.inject import Singleton
from micronaut.context.annotation import Requires
from micronaut.mcp.annotations import Tool


@Requires(property="spec.name", value="DiskSpaceMcpServer")  # <1>
@Singleton
class DiskSpaceTools:

    @Tool(name="freeDiskSpace", description="Return the free disk space in the users computer")  # <2>
    def free_disk_space(self) -> str:
        return "Free disk space: 42 GB"
