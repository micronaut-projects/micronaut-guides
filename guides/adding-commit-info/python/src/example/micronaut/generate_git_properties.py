from pathlib import Path
import subprocess


# tag::clazz[]
def generate_git_properties(project_dir: Path | None = None) -> Path:
    root = (project_dir or Path.cwd()).resolve()
    output = root / "config" / "git.properties"
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
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text(
        "".join(f"{key}={_property_value(value)}\n" for key, value in values.items()),
        encoding="ascii",
    )
    return output


def _git(root: Path, *args: str) -> str:
    return subprocess.run(
        ["git", "-C", str(root), *args],
        check=True,
        stdout=subprocess.PIPE,
    ).stdout.decode("utf-8").removesuffix("\n")


def _property_value(value: str) -> str:
    escapes = {
        "\\": "\\\\", "\t": "\\t", "\n": "\\n", "\r": "\\r", "\f": "\\f",
        " ": "\\ ", "=": "\\=", ":": "\\:", "#": "\\#", "!": "\\!",
    }
    result = []
    for character in value:
        if character in escapes:
            result.append(escapes[character])
        elif "!" <= character <= "~":
            result.append(character)
        else:
            encoded = character.encode("utf-16-be").hex()
            result.extend("\\u" + encoded[i:i + 4] for i in range(0, len(encoded), 4))
    return "".join(result)
# end::clazz[]


if __name__ == "__main__":
    generate_git_properties()
