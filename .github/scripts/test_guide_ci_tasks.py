#!/usr/bin/env python3
from __future__ import annotations

import json
import subprocess
import sys
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

    def test_default_python_build_remains_unchanged(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "python-guide", languages=["JAVA", "PYTHON"], apps=[{"name": "default"}])

            self.assertEqual(
                ["pythonGuideBuild"],
                guide_ci_tasks.tasks_for_changed_files(
                    ["guides/python-guide/metadata.json"], guides_dir,
                ),
            )

    def test_jvm_only_guide_without_apps_has_no_absent_runner_exclusion(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "python-guide", languages=["JAVA", "PYTHON"])

            self.assertEqual(
                ["pythonGuideBuild"],
                guide_ci_tasks.tasks_for_changed_files(
                    ["guides/python-guide/metadata.json"], guides_dir, jvm_only=True,
                ),
            )

    def test_jvm_only_excludes_python_runner_for_explicit_apps(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(
                guides_dir, "python-guide", languages=["JAVA", "PYTHON"],
                apps=[{"name": "books"}, {"name": "inventory"}],
            )

            self.assertEqual(
                ["pythonGuideBuild -x pythonGuideRunPythonTestScript"],
                guide_ci_tasks.tasks_for_changed_files(
                    ["guides/python-guide/python/tests/test_books.py"], guides_dir, jvm_only=True,
                ),
            )

    def test_jvm_only_docs_only_guide_has_no_absent_runner_exclusion(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "docs-guide", languages=["JAVA", "PYTHON"], apps=[])

            self.assertEqual(
                ["docsGuideBuild"],
                guide_ci_tasks.tasks_for_changed_files(
                    ["guides/docs-guide/docs-guide.adoc"], guides_dir, jvm_only=True,
                ),
            )

    def test_jvm_only_retains_jvm_guide_build(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "jvm-guide", languages=["JAVA", "GROOVY", "KOTLIN"])

            self.assertEqual(
                ["jvmGuideBuild"],
                guide_ci_tasks.tasks_for_changed_files(
                    ["guides/jvm-guide/metadata.json"], guides_dir, jvm_only=True,
                ),
            )

    def test_jvm_only_excludes_python_runner_for_inherited_apps(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "app-base", publish=False, apps=[{"name": "default"}])
            write_metadata(guides_dir, "python-guide", base="app-base", languages=["PYTHON"], apps=[])

            self.assertEqual(
                ["pythonGuideBuild -x pythonGuideRunPythonTestScript"],
                guide_ci_tasks.tasks_for_changed_files(
                    ["guides/python-guide/metadata.json"], guides_dir, jvm_only=True,
                ),
            )

    def test_jvm_only_resolves_sorted_transitive_app_merges(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "a-base", publish=False, apps=[{"name": "default"}])
            write_metadata(guides_dir, "b-base", publish=False, base="a-base", apps=[])
            write_metadata(guides_dir, "python-guide", base="b-base", languages=["PYTHON"], apps=[])

            self.assertEqual(
                ["pythonGuideBuild -x pythonGuideRunPythonTestScript"],
                guide_ci_tasks.tasks_for_changed_files(
                    ["guides/a-base/common.adoc"], guides_dir, jvm_only=True,
                ),
            )

    def test_jvm_only_does_not_inherit_base_python_language(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(
                guides_dir, "python-base", publish=False, languages=["PYTHON"],
                apps=[{"name": "default"}],
            )
            for languages in (None, ["JAVA", "GROOVY", "KOTLIN"]):
                with self.subTest(languages=languages):
                    child = "default-jvm" if languages is None else "explicit-jvm"
                    write_metadata(guides_dir, child, base="python-base", languages=languages, apps=[])
                    self.assertEqual(
                        [f"{guide_ci_tasks.kebab_case_to_gradle_name(child)}Build"],
                        guide_ci_tasks.tasks_for_changed_files(
                            [f"guides/{child}/metadata.json"], guides_dir, jvm_only=True,
                        ),
                    )

    def test_jvm_only_empty_base_and_child_have_no_python_runner(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "docs-base", publish=False, languages=["PYTHON"], apps=[])
            write_metadata(guides_dir, "docs-guide", base="docs-base", languages=["PYTHON"], apps=[])

            self.assertEqual(
                ["docsGuideBuild"],
                guide_ci_tasks.tasks_for_changed_files(
                    ["guides/docs-base/common.adoc"], guides_dir, jvm_only=True,
                ),
            )

    def test_jvm_only_preserves_sorted_merge_order_for_later_bases(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "z-base", publish=False, apps=[{"name": "default"}])
            write_metadata(guides_dir, "b-base", publish=False, base="z-base", apps=[])
            write_metadata(guides_dir, "a-guide", base="b-base", languages=["PYTHON"], apps=[])

            self.assertEqual(
                ["aGuideBuild"],
                guide_ci_tasks.tasks_for_changed_files(
                    ["guides/z-base/common.adoc"], guides_dir, jvm_only=True,
                ),
            )

    def test_jvm_only_preserves_transitive_base_impacts_and_deduplication(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            guides_dir = Path(temp_dir)
            write_metadata(guides_dir, "root-base", publish=False)
            write_metadata(guides_dir, "middle-base", publish=False, base="root-base")
            write_metadata(
                guides_dir, "python-guide", base="middle-base", languages=["JAVA", "PYTHON"],
                apps=[{"name": "default"}],
            )

            self.assertEqual(
                ["pythonGuideBuild -x pythonGuideRunPythonTestScript"],
                guide_ci_tasks.tasks_for_changed_files(
                    ["guides/root-base/common.adoc", "guides/python-guide/metadata.json"],
                    guides_dir, jvm_only=True,
                ),
            )

    def test_cli_jvm_only_flag(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            repo = Path(temp_dir)
            guides_dir = repo / "guides"
            guides_dir.mkdir()
            write_metadata(guides_dir, "python-guide", languages=["JAVA", "PYTHON"], apps=[{"name": "default"}])

            for flags, task in (
                ([], "pythonGuideBuild"),
                (["--jvm-only"], "pythonGuideBuild -x pythonGuideRunPythonTestScript"),
            ):
                with self.subTest(flags=flags):
                    result = subprocess.run(
                        [sys.executable, str(Path(guide_ci_tasks.__file__).resolve()),
                         "--repo", str(repo), "--changed-file", "guides/python-guide/metadata.json", *flags],
                        capture_output=True, text=True,
                    )

                    self.assertEqual(0, result.returncode, result.stderr)
                    self.assertEqual({"group_test_tasks": [task]}, json.loads(result.stdout))


def write_metadata(
    guides_dir: Path,
    slug: str,
    *,
    publish: bool = True,
    base: str | None = None,
    languages: list[str] | None = None,
    apps: list[dict[str, str]] | None = None,
) -> None:
    guide_dir = guides_dir / slug
    guide_dir.mkdir()
    metadata = {"title": slug}
    if publish is not True:
        metadata["publish"] = publish
    if base is not None:
        metadata["base"] = base
    if languages is not None:
        metadata["languages"] = languages
    if apps is not None:
        metadata["apps"] = apps
    (guide_dir / "metadata.json").write_text(json.dumps(metadata), encoding="utf-8")


if __name__ == "__main__":
    unittest.main()
