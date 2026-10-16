import pytest

from example.micronaut.e164_utils import is_valid


@pytest.mark.parametrize(
    "phone",
    [
        "+04630443322",
        "+1415555267102345",
        "+1-4155552671",
        "",
    ],
)
def test_invalid_phones(phone):
    assert not is_valid(phone)


@pytest.mark.parametrize(
    "phone",
    [
        "+14155552671",
        "+442071838750",
        "+55115525632",
        "14155552671",
        "442071838750",
        "55115525632",
    ],
)
def test_valid_phones(phone):
    assert is_valid(phone)
