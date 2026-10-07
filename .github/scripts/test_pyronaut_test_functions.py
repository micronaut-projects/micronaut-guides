from __future__ import annotations

import os
import subprocess
import tempfile
import unittest
from pathlib import Path


HELPER = Path(__file__).resolve().parents[2] / "buildSrc/src/main/resources/pyronaut-test-functions.sh"
STEPS = ("setup", "install", "validate-config", "process", "test")
HARNESS = r'''
set -e
source "$1"
set_pyronaut_test_resources() {
  printf 'setup\n' >> "$COMMAND_LOG"
  if [[ "$FAILED_STEP" == "setup" ]]; then return 42; fi
}
pyronaut() {
  printf '%s\n' "$1" >> "$COMMAND_LOG"
  if [[ "$FAILED_STEP" == "$1" ]]; then return 42; fi
}
run_pyronaut_install() { pyronaut install; }
run_pyronaut_validate_config() { pyronaut validate-config; }
run_pyronaut_process() { pyronaut process; }
run_pyronaut_test() { pyronaut test; }
status=0
run_pyronaut_tests || status=$?
exit "$status"
'''


class PyronautTestFunctionsTest(unittest.TestCase):
    def run_helper(self, failed_step: str):
        with tempfile.TemporaryDirectory() as directory:
            command_log = Path(directory) / "commands.log"
            completed = subprocess.run(
                ["/bin/bash", "-c", HARNESS, "helper-test", str(HELPER)],
                cwd=directory,
                env={"PATH": os.defpath, "COMMAND_LOG": str(command_log), "FAILED_STEP": failed_step},
                text=True, capture_output=True, timeout=5,
            )
            commands = command_log.read_text().splitlines() if command_log.exists() else []
        return completed, commands

    def check_failure(self, step: str):
        completed, commands = self.run_helper(step)
        self.assertEqual(42, completed.returncode, f"commands={commands}, stderr={completed.stderr}")
        self.assertEqual(list(STEPS[:STEPS.index(step) + 1]), commands)

    def test_setup_failure_stops_install_and_tests(self):
        self.check_failure("setup")

    def test_install_failure_stops_validation_and_tests(self):
        self.check_failure("install")

    def test_validation_failure_stops_processing_and_tests(self):
        self.check_failure("validate-config")

    def test_processing_failure_stops_tests(self):
        self.check_failure("process")

    def test_test_failure_is_propagated(self):
        self.check_failure("test")

    def test_success_executes_every_step_once(self):
        completed, commands = self.run_helper("")
        self.assertEqual(0, completed.returncode, completed.stderr)
        self.assertEqual(list(STEPS), commands)


if __name__ == "__main__":
    unittest.main()
