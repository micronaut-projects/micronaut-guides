pyronaut_repo_python () {
  if [ -n "${PYTHON:-}" ]; then
    echo "$PYTHON"
  elif command -v python3 > /dev/null 2>&1; then
    echo "python3"
  else
    echo "python"
  fi
}

set_pyronaut_test_resources () {
  "$(pyronaut_repo_python)" -c 'exec(__import__("sys").stdin.read())' <<'PY'
from pathlib import Path
import re

pyproject = Path("pyproject.toml")
text = pyproject.read_text(encoding="utf-8")
section = "[tool.pyronaut.test-resources]"
section_index = text.find(section)
if section_index < 0:
    raise SystemExit(f"Missing {section} in pyproject.toml")

next_section_index = text.find("\n[", section_index + len(section))
if next_section_index < 0:
    next_section_index = len(text)
section_text = text[section_index:next_section_index]
section_text, count = re.subn(r'(?m)^enabled\s*=\s*(?:true|false)', "enabled = true", section_text, count=1)
if count == 0:
    section_text = section_text.replace(section + "\n", section + "\nenabled = true\n", 1)
text = text[:section_index] + section_text + text[next_section_index:]
pyproject.write_text(text, encoding="utf-8")
PY
}

run_pyronaut_tests () {
  set_pyronaut_test_resources &&
    pyronaut install &&
    pyronaut validate-config &&
    pyronaut process &&
    pyronaut test
}
