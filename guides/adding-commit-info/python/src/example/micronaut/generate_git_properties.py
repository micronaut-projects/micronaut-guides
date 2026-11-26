"""Write config/git.properties, the file the /info endpoint reads, from the current Git commit."""

from pathlib import Path
import subprocess

ESCAPES = {"\\": r"\\", "\n": r"\n", "\r": r"\r", "\t": r"\t", "=": r"\=", ":": r"\:", "#": r"\#", "!": r"\!"}


def git(*args, cwd="."):
    return subprocess.check_output(["git", *args], cwd=cwd, text=True).strip()


def git_info(project_dir="."):
    """Collect the values the /info endpoint exposes under the git key."""
    def commit(placeholder):
        return git("log", "-1", f"--format={placeholder}", cwd=project_dir)

    return {
        "git.branch": git("rev-parse", "--abbrev-ref", "HEAD", cwd=project_dir),
        "git.commit.id": commit("%H"),
        "git.commit.id.abbrev": commit("%h"),
        "git.commit.id.describe": git("describe", "--always", "--dirty", cwd=project_dir),
        "git.commit.time": commit("%ct"),
        "git.commit.message.short": commit("%s"),
        "git.commit.message.full": commit("%B"),
        "git.commit.user.name": commit("%an"),
        "git.commit.user.email": commit("%ae"),
        "git.dirty": str(bool(git("status", "--porcelain", cwd=project_dir))).lower(),
    }


def _escape(value):
    # The /info endpoint reads git.properties as Latin-1 rather than UTF-8, so separators and control
    # characters are escaped and every character outside ASCII is written as \uXXXX UTF-16 code units.
    escaped = ""
    for char in value:
        if char in ESCAPES:
            escaped += ESCAPES[char]
        elif " " <= char <= "~":
            escaped += char
        else:
            units = char.encode("utf-16-be").hex()
            escaped += "".join(f"\\u{units[i:i + 4]}" for i in range(0, len(units), 4))
    return "\\" + escaped if escaped.startswith(" ") else escaped


def generate_git_properties(project_dir="."):
    """Write config/git.properties for the repository in project_dir and return its path."""
    output = Path(project_dir, "config", "git.properties")
    output.parent.mkdir(exist_ok=True)
    output.write_text("".join(f"{key}={_escape(value)}\n" for key, value in git_info(project_dir).items()))
    return output


if __name__ == "__main__":
    print(f"Wrote {generate_git_properties()}")
