import sys
from pathlib import Path

from setuptools import setup
from setuptools.command.build_py import build_py


ROOT = Path(__file__).parent
sys.path.insert(0, str(ROOT / "src"))

from example.micronaut.generate_git_properties import generate_git_properties


class BuildWithGitProperties(build_py):
    def run(self):
        generate_git_properties(ROOT)
        super().run()


setup(cmdclass={"build_py": BuildWithGitProperties})
