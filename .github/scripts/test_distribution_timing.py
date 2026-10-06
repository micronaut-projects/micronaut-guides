from __future__ import annotations

import os
import subprocess
import tempfile
import unittest
from pathlib import Path


BASE = Path(__file__).resolve().parents[2] / "guides/distribution-base"
LANGUAGES = ("python", "java", "groovy", "kotlin")
JVM_BRANCHES = (("java", "java"), ("java", "crac"), ("groovy", "java"), ("kotlin", "java"))
HARNESS = r'''
if [[ "$INHERIT_ERREXIT" == "yes" ]]; then
  set -o posix
fi
curl() {
  if [[ "$TIMING_RESULT" == "timeout" ]]; then
    SECONDS=$((SECONDS + 21))
    return 1
  fi
  return 0
}
sleep() { :; }
docker() {
  printf 'docker %s\n' "$*" >> "$COMMAND_LOG"
  if [[ "$1" == "run" ]]; then
    printf 'fake-container\n'
  fi
}
native_fake() { printf 'native start\n' >> "$COMMAND_LOG"; }
java() { printf 'java start %s\n' "$*" >> "$COMMAND_LOG"; }
kill() { printf 'native cleanup %s\n' "$*" >> "$COMMAND_LOG"; }
source "$1" "${@:2}"
'''


class DistributionTimingTest(unittest.TestCase):
    def run_timing(self, language: str, branch: str, result: str, inherit_errexit: bool = False):
        with tempfile.TemporaryDirectory() as directory:
            command_log = Path(directory) / "commands.log"
            environment = os.environ | {
                "COMMAND_LOG": str(command_log),
                "TIMING_RESULT": result,
                "INHERIT_ERREXIT": "yes" if inherit_errexit else "no",
            }
            arguments = {
                "docker": ["-d", "fake-image"], "native": ["-n", "native_fake"],
                "java": ["-j", "fake.jar"], "crac": ["-c", "fake-checkpoint"],
            }[branch]
            completed = subprocess.run(
                ["bash", "-c", HARNESS, "timing-test", str(BASE / language / "ttfr.sh"), *arguments],
                env=environment, text=True, capture_output=True, timeout=5,
            )
            commands = command_log.read_text() if command_log.exists() else ""
        cleanup = "docker container kill fake-container" if branch == "docker" else "native cleanup"
        return completed, commands, cleanup

    def check_timeout(self, language: str, branch: str, inherit_errexit: bool = False):
        completed, commands, cleanup = self.run_timing(language, branch, "timeout", inherit_errexit)
        self.assertIn("No response from the app in 20 seconds", completed.stderr)
        self.assertNotEqual(0, completed.returncode, completed.stdout)
        self.assertIn(cleanup, commands)
        self.assertNotIn(" seconds", completed.stdout)

    def test_docker_timeout(self):
        for language in LANGUAGES:
            with self.subTest(language=language):
                self.check_timeout(language, "docker")

    def test_native_timeout(self):
        for language in LANGUAGES:
            with self.subTest(language=language):
                self.check_timeout(language, "native")

    def test_docker_timeout_with_inherited_errexit(self):
        for language in LANGUAGES:
            with self.subTest(language=language):
                self.check_timeout(language, "docker", True)

    def test_native_timeout_with_inherited_errexit(self):
        for language in LANGUAGES:
            with self.subTest(language=language):
                self.check_timeout(language, "native", True)

    def test_jvm_timeout(self):
        for language, branch in JVM_BRANCHES:
            with self.subTest(language=language, branch=branch):
                self.check_timeout(language, branch)

    def test_jvm_timeout_with_inherited_errexit(self):
        for language, branch in JVM_BRANCHES:
            with self.subTest(language=language, branch=branch):
                self.check_timeout(language, branch, True)

    def check_success(self, language: str, branch: str):
        completed, commands, cleanup = self.run_timing(language, branch, "success")
        self.assertEqual(0, completed.returncode, completed.stderr)
        self.assertEqual(1, commands.count(cleanup))
        self.assertIn(" seconds", completed.stdout)

    def test_docker_success(self):
        for language in LANGUAGES:
            with self.subTest(language=language):
                self.check_success(language, "docker")

    def test_native_success(self):
        for language in LANGUAGES:
            with self.subTest(language=language):
                self.check_success(language, "native")

    def test_jvm_success(self):
        for language, branch in JVM_BRANCHES:
            with self.subTest(language=language, branch=branch):
                self.check_success(language, branch)


if __name__ == "__main__":
    unittest.main()
