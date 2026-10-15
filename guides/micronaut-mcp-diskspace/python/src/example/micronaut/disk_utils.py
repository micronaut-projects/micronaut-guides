from pathlib import Path
import shutil


def free_disk_space() -> str:
    usage = shutil.disk_usage(Path("/"))
    free_gb = usage.free / (1024.0 * 1024 * 1024)
    return f"Free disk space: {free_gb:.2f} GB"
