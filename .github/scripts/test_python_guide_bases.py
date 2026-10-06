from __future__ import annotations

import ast
import unittest
from pathlib import Path


GUIDES = Path(__file__).resolve().parents[2] / "guides"


class PythonGuideBasesTest(unittest.TestCase):
    def test_openapi_toml_example_is_python_only(self) -> None:
        source = (GUIDES / "micronaut-openapi-base/openapi-intro.adoc").read_text(encoding="utf-8")
        self.assertIn(
            ":only-for-languages:python\nresource:application.toml[tag=static-resources-swagger]\n:only-for-languages:",
            source,
        )

    def test_storage_download_contract_allows_missing_object(self) -> None:
        source = GUIDES / "micronaut-object-storage-base/python/src/example/micronaut/profile_pictures_api.py"
        module = ast.parse(source.read_text(encoding="utf-8"))
        api = next(node for node in module.body if isinstance(node, ast.ClassDef) and node.name == "ProfilePicturesApi")
        download = next(node for node in api.body if isinstance(node, ast.FunctionDef) and node.name == "download")
        self.assertEqual("HttpResponse[StreamedFile] | None", ast.unparse(download.returns))

if __name__ == "__main__":
    unittest.main()
