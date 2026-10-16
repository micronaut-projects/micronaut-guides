from pathlib import Path
import subprocess


# tag::clazz[]
def generate_git_properties(project_dir: Path | None = None) -> Path:
    root = (project_dir or Path.cwd()).resolve()
    output = root / "config" / "git.properties"
    output.parent.mkdir(parents=True, exist_ok=True)

    values = {
        "git.branch": _git(root, "rev-parse", "--abbrev-ref", "HEAD"),
        "git.commit.id": _git(root, "rev-parse", "HEAD"),
        "git.commit.message.short": _git(root, "show", "-s", "--format=%s", "HEAD"),
        "git.commit.message.full": _git(root, "show", "-s", "--format=%B", "HEAD"),
        "git.commit.time": _git(root, "show", "-s", "--format=%ct", "HEAD"),
        "git.commit.user.name": _git(root, "show", "-s", "--format=%an", "HEAD"),
        "git.commit.user.email": _git(root, "show", "-s", "--format=%ae", "HEAD"),
        "git.dirty": str(bool(_git(root, "status", "--porcelain"))).lower(),
    }
    output.write_text(
        "".join(f"{key}={_property_value(value)}\n" for key, value in values.items()),
        encoding="utf-8",
    )
    return output


def _git(root: Path, *args: str) -> str:
    result = subprocess.run(
        ["git", "-C", str(root), *args],
        check=True,
        stdout=subprocess.PIPE,
        text=True,
    )
    return result.stdout.strip()


def _property_value(value: str) -> str:
    return value.replace("\\", "\\\\").replace("\n", "\\n")
# end::clazz[]
