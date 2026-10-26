from pathlib import Path


def property_keys(path: str) -> set[str]:
    return {
        line.split("=", 1)[0]
        for line in Path(path).read_text(encoding="utf-8").splitlines()
        if line.strip() and not line.lstrip().startswith("#")
    }


def test_keys_are_localized_in_every_language():
    default_keys = property_keys("config/i18n/messages_en.properties")
    suffixes = ["es"]  # <1>
    for suffix in suffixes:
        localized_keys = property_keys(f"config/i18n/messages_{suffix}.properties")
        assert default_keys == localized_keys  # <2>
