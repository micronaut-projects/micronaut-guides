from jakarta.inject import Singleton
from micronaut.mcp.annotations import Tool

from example.micronaut.disk_utils import free_disk_space


@Singleton  # <1>
class MyTools:
    @Tool(
        title="Free Disk Space",
        description="Return the free disk space in the users computer",
    )  # <2>
    def freeDiskSpace(self) -> str:
        return free_disk_space()
