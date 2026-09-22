pyronaut_repo_python () {
  if [ -n "${PYTHON:-}" ]; then
    echo "$PYTHON"
  elif command -v python3 > /dev/null 2>&1; then
    echo "python3"
  else
    echo "python"
  fi
}

set_pyronaut_local_repositories () {
  if [ -z "${PYRONAUT_LOCAL_REPOSITORY:-}" ]; then
    return 0
  fi

  "$(pyronaut_repo_python)" -c 'exec(__import__("sys").stdin.read())' "$PYRONAUT_LOCAL_REPOSITORY" <<'PY'
from pathlib import Path
import json
import re
import sys

repository = sys.argv[1]
pyproject = Path("pyproject.toml")
text = pyproject.read_text(encoding="utf-8")
section = "[tool.pyronaut]"
section_index = text.find(section)
if section_index < 0:
    raise SystemExit("Missing [tool.pyronaut] in pyproject.toml")

next_section_index = text.find("\n[", section_index + len(section))
if next_section_index < 0:
    next_section_index = len(text)
section_text = text[section_index:next_section_index]
repositories = f'repositories = [{json.dumps(repository)}, "mavenCentral", "https://central.sonatype.com/repository/maven-snapshots/"]'
section_text, count = re.subn(r'(?ms)^repositories\s*=\s*\[[^\]]*\]', repositories, section_text, count=1)
if count == 0:
    section_text = section_text.replace(section + "\n", section + "\n" + repositories + "\n", 1)
text = text[:section_index] + section_text + text[next_section_index:]
pyproject.write_text(text, encoding="utf-8")
PY
}

set_pyronaut_local_versions () {
  if [ -z "${PYRONAUT_LOCAL_CORE_VERSION:-}" ] && [ -z "${PYRONAUT_LOCAL_PLATFORM_VERSION:-}" ]; then
    return 0
  fi

  "$(pyronaut_repo_python)" -c 'exec(__import__("sys").stdin.read())' "${PYRONAUT_LOCAL_CORE_VERSION:-}" "${PYRONAUT_LOCAL_PLATFORM_VERSION:-}" <<'PY'
from pathlib import Path
import re
import sys

core_version = sys.argv[1]
platform_version = sys.argv[2]
pyproject = Path("pyproject.toml")
text = pyproject.read_text(encoding="utf-8")

def set_section_version(source, section, version):
    if not version:
        return source
    section_index = source.find(section)
    if section_index < 0:
        raise SystemExit(f"Missing {section} in pyproject.toml")

    next_section_index = source.find("\n[", section_index + len(section))
    if next_section_index < 0:
        next_section_index = len(source)
    section_text = source[section_index:next_section_index]
    replacement = f'version = "{version}"'
    section_text, count = re.subn(r'(?m)^version\s*=\s*"[^"]*"', replacement, section_text, count=1)
    if count == 0:
        section_text = section_text.replace(section + "\n", section + "\n" + replacement + "\n", 1)
    return source[:section_index] + section_text + source[next_section_index:]

text = set_section_version(text, "[tool.pyronaut.core]", core_version)
text = set_section_version(text, "[tool.pyronaut.platform]", platform_version)
pyproject.write_text(text, encoding="utf-8")
PY
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

set_pyronaut_native_toolchain () {
  if [ "${PYRONAUT_USE_NATIVE_TOOLCHAIN:-}" != "true" ]; then
    return 0
  fi

  "$(pyronaut_repo_python)" -c 'exec(__import__("sys").stdin.read())' <<'PY'
from pathlib import Path

pyproject = Path("pyproject.toml")
text = pyproject.read_text(encoding="utf-8")
section = "[tool.pyronaut.toolchain]"
section_index = text.find(section)
if section_index < 0:
    separator = "" if not text or text.endswith("\n") else "\n"
    text += f'{separator}\n{section}\ntype = "native"\n'
else:
    next_section_index = text.find("\n[", section_index + len(section))
    if next_section_index < 0:
        next_section_index = len(text)
    section_text = text[section_index:next_section_index]
    lines = section_text.splitlines(keepends=True)
    for index, line in enumerate(lines):
        if line.lstrip().startswith('type') and '=' in line:
            line_ending = '\n' if line.endswith('\n') else ''
            lines[index] = 'type = "native"' + line_ending
            break
    else:
        lines.insert(1, 'type = "native"\n')
    section_text = "".join(lines)
    text = text[:section_index] + section_text + text[next_section_index:]
pyproject.write_text(text, encoding="utf-8")
PY
}

set_pyronaut_project_venv () {
  if [ -e .venv ] || [ -L .venv ] || [ -z "${VIRTUAL_ENV:-}" ] || [ ! -x "$VIRTUAL_ENV/bin/python" ]; then
    return 0
  fi
  ln -s "$VIRTUAL_ENV" .venv
}

pyronaut_dependency_cache () {
  echo "${PYRONAUT_DEPENDENCY_CACHE:-.pyronaut-m2}"
}

run_pyronaut_install () {
  local install_args=()
  if [ -n "${PYRONAUT_LOCAL_REPOSITORY:-}" ]; then
    install_args+=(--local-repository "$(pyronaut_dependency_cache)")
    if [ "${PYRONAUT_REFRESH_DEPENDENCIES:-true}" = "true" ]; then
      install_args+=(--refresh)
    fi
  fi

  if [ -n "${PYRONAUT_INSTALL_EXECUTABLE:-}" ]; then
    "$PYRONAUT_INSTALL_EXECUTABLE" "${install_args[@]}"
  else
    pyronaut install "${install_args[@]}"
  fi
}

run_pyronaut_validate_config () {
  if [ -n "${PYRONAUT_VALIDATE_CONFIG_EXECUTABLE:-}" ]; then
    "$PYRONAUT_VALIDATE_CONFIG_EXECUTABLE"
  else
    pyronaut validate-config
  fi
}

run_pyronaut_process () {
  if [ -n "${PYRONAUT_PROCESS_EXECUTABLE:-}" ]; then
    "$PYRONAUT_PROCESS_EXECUTABLE"
  else
    pyronaut process
  fi
}

run_pyronaut_cli () {
  if [ -n "${PYRONAUT_CLI_PYTHONPATH:-}" ] && [ -d "$PYRONAUT_CLI_PYTHONPATH" ]; then
    PYTHONPATH="${PYRONAUT_CLI_PYTHONPATH}${PYTHONPATH:+:$PYTHONPATH}" "$(pyronaut_repo_python)" -m pyronaut_cli_v2 "$@"
  else
    pyronaut "$@"
  fi
}

run_pyronaut_test () {
  local test_args=()
  if [ -n "${PYRONAUT_LOCAL_REPOSITORY:-}" ]; then
    test_args+=(--local-repository "$(pyronaut_dependency_cache)")
  fi
  run_pyronaut_cli test "${test_args[@]}"
}

run_pyronaut_tests () {
  if [ -z "${PYRONAUT_LOCAL_REPOSITORY:-}" ]; then
    set_pyronaut_test_resources
    set_pyronaut_native_toolchain
    set_pyronaut_project_venv
    run_pyronaut_install && run_pyronaut_validate_config && run_pyronaut_process && run_pyronaut_test
    return $?
  fi

  local tmp_dir
  tmp_dir="$(mktemp -d "${TMPDIR:-/tmp}/pyronaut-guide.XXXXXX")"
  tar --exclude='./__pyronaut__' --exclude='./.pytest_cache' --exclude='./build' --exclude='./dist' --exclude='./*.egg-info' -cf - . | (cd "$tmp_dir" && tar -xf -)
  (
    cd "$tmp_dir"
    set_pyronaut_local_repositories
    set_pyronaut_local_versions
    set_pyronaut_test_resources
    set_pyronaut_native_toolchain
    set_pyronaut_project_venv
    run_pyronaut_install && run_pyronaut_validate_config && run_pyronaut_process && run_pyronaut_test
  )
  local result=$?
  rm -rf "$tmp_dir"
  return $result
}
