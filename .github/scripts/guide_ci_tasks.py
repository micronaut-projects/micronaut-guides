#!/usr/bin/env python3
from __future__ import annotations

import argparse
import json
import subprocess
import sys
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable


NULL_SHA = "0" * 40


@dataclass(frozen=True)
class GuideMetadata:
    slug: str
    publish: bool
    base: str | None


def kebab_case_to_gradle_name(name: str) -> str:
    gradle_name = "".join(part[:1].upper() + part[1:] for part in name.split("-"))
    return gradle_name[:1].lower() + gradle_name[1:]


def guide_slug_from_path(path: str) -> str | None:
    parts = path.strip().strip("/").split("/")
    if len(parts) >= 2 and parts[0] == "guides" and parts[1]:
        return parts[1]
    return None


def guide_slugs_from_paths(paths: Iterable[str]) -> set[str]:
    return {
        slug
        for path in paths
        if (slug := guide_slug_from_path(path)) is not None
    }


def load_guides(guides_dir: Path) -> dict[str, GuideMetadata]:
    guides = {}
    for metadata_path in sorted(guides_dir.glob("*/metadata.json")):
        with metadata_path.open(encoding="utf-8") as metadata_file:
            metadata = json.load(metadata_file)
        slug = metadata_path.parent.name
        guides[slug] = GuideMetadata(
            slug=slug,
            publish=metadata.get("publish", True) is not False,
            base=metadata.get("base"),
        )
    return guides


def resolve_impacted_guide_slugs(
    changed_files: Iterable[str],
    guides: dict[str, GuideMetadata],
) -> list[str]:
    impacted_slugs = guide_slugs_from_paths(changed_files)

    changed = True
    while changed:
        changed = False
        for guide in guides.values():
            if guide.base in impacted_slugs and guide.slug not in impacted_slugs:
                impacted_slugs.add(guide.slug)
                changed = True

    return sorted(
        guide.slug
        for guide in guides.values()
        if guide.publish and guide.slug in impacted_slugs
    )


def tasks_for_changed_files(changed_files: Iterable[str], guides_dir: Path) -> list[str]:
    guides = load_guides(guides_dir)
    return [
        f"{kebab_case_to_gradle_name(slug)}Build"
        for slug in resolve_impacted_guide_slugs(changed_files, guides)
    ]


def matrix_for_tasks(tasks: Iterable[str]) -> dict[str, list[str]]:
    return {"group_test_tasks": list(tasks)}


def git_changed_files(
    repo: Path,
    base: str | None,
    head: str,
    diff_mode: str = "range",
) -> list[str]:
    if base and base != NULL_SHA:
        revisions = [f"{base}...{head}"] if diff_mode == "merge-base" else [base, head]
        command = ["git", "diff", "--name-only", "--diff-filter=ACMRT", *revisions]
    else:
        command = ["git", "diff-tree", "--no-commit-id", "--name-only", "-r", head]

    result = subprocess.run(
        command,
        cwd=repo,
        check=True,
        text=True,
        stdout=subprocess.PIPE,
    )
    return [line for line in result.stdout.splitlines() if line]


def write_github_output(output_path: Path, matrix: dict[str, list[str]]) -> None:
    tasks = matrix["group_test_tasks"]
    with output_path.open("a", encoding="utf-8") as output_file:
        output_file.write(f"matrix={json.dumps(matrix, separators=(',', ':'))}\n")
        output_file.write(f"has_tasks={str(bool(tasks)).lower()}\n")


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Resolve changed guide paths to Micronaut guide Gradle build tasks.",
    )
    parser.add_argument("--repo", default=".", help="Repository root.")
    parser.add_argument("--guides-dir", help="Guides directory. Defaults to <repo>/guides.")
    parser.add_argument("--base", help="Base commit for git diff.")
    parser.add_argument("--head", default="HEAD", help="Head commit for git diff.")
    parser.add_argument(
        "--diff-mode",
        choices=("range", "merge-base"),
        default="range",
        help="Use range for base..head or merge-base for base...head.",
    )
    parser.add_argument(
        "--changed-file",
        action="append",
        dest="changed_files",
        help="Changed file path. May be supplied more than once; skips git diff.",
    )
    parser.add_argument(
        "--github-output",
        help="Path to the GitHub Actions output file.",
    )
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    repo = Path(args.repo).resolve()
    guides_dir = Path(args.guides_dir).resolve() if args.guides_dir else repo / "guides"

    changed_files = (
        args.changed_files
        if args.changed_files is not None
        else git_changed_files(repo, args.base, args.head, args.diff_mode)
    )
    tasks = tasks_for_changed_files(changed_files, guides_dir)
    matrix = matrix_for_tasks(tasks)

    print(f"Changed files: {json.dumps(changed_files)}", file=sys.stderr)
    print(f"Selected Gradle tasks: {json.dumps(tasks)}", file=sys.stderr)
    print(json.dumps(matrix, separators=(",", ":")))

    if args.github_output:
        write_github_output(Path(args.github_output), matrix)

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
