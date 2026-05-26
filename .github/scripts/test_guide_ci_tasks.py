#!/usr/bin/env python3
from __future__ import annotations

import json
import tempfile
import unittest
from pathlib import Path

import guide_ci_tasks


class GuideCiTasksTest(unittest.TestCase):
    def test_direct_guide_change_maps_to_build_task(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "micronaut-data-mongodb-synchronous")

            tasks = guide_ci_tasks.tasks_for_changed_files(
                ["guides/micronaut-data-mongodb-synchronous/src/main/java/Book.java"],
                guides_dir,
            )

            self.assertEqual(["micronautDataMongodbSynchronousBuild"], tasks)

    def test_base_guide_change_maps_to_dependent_published_guide(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "hello-base", publish=False)
            write_metadata(guides_dir, "creating-your-first-micronaut-app", base="hello-base")

            tasks = guide_ci_tasks.tasks_for_changed_files(
                ["guides/hello-base/src/main/java/example/micronaut/MessageController.java"],
                guides_dir,
            )

            self.assertEqual(["creatingYourFirstMicronautAppBuild"], tasks)

    def test_transitive_base_guide_changes_are_resolved(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "root-base", publish=False)
            write_metadata(guides_dir, "intermediate-base", publish=False, base="root-base")
            write_metadata(guides_dir, "published-guide", base="intermediate-base")

            tasks = guide_ci_tasks.tasks_for_changed_files(
                ["guides/root-base/common.adoc"],
                guides_dir,
            )

            self.assertEqual(["publishedGuideBuild"], tasks)

    def test_multiple_changed_files_deduplicate_tasks(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "hello-base", publish=False)
            write_metadata(guides_dir, "creating-your-first-micronaut-app", base="hello-base")

            tasks = guide_ci_tasks.tasks_for_changed_files(
                [
                    "guides/creating-your-first-micronaut-app/metadata.json",
                    "guides/creating-your-first-micronaut-app/src/main/java/Application.java",
                    "guides/hello-base/src/main/java/MessageController.java",
                ],
                guides_dir,
            )

            self.assertEqual(["creatingYourFirstMicronautAppBuild"], tasks)

    def test_non_guide_changes_return_empty_task_list(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "micronaut-data-mongodb-synchronous")

            tasks = guide_ci_tasks.tasks_for_changed_files(
                [".github/workflows/gradle.yml", "buildSrc/src/main/groovy/Plugin.groovy"],
                guides_dir,
            )

            self.assertEqual([], tasks)


def write_metadata(
    guides_dir: Path,
    slug: str,
    *,
    publish: bool = True,
    base: str | None = None,
) -> None:
    guide_dir = guides_dir / slug
    guide_dir.mkdir()
    metadata = {"title": slug}
    if publish is not True:
        metadata["publish"] = publish
    if base is not None:
        metadata["base"] = base
    (guide_dir / "metadata.json").write_text(json.dumps(metadata), encoding="utf-8")


if __name__ == "__main__":
    unittest.main()
