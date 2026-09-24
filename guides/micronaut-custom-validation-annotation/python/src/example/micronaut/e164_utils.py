from .country_code import CountryCode

MAX_NUMBER_OF_DIGITS = 15
PLUS_SIGN = "+"


def is_valid(value: str | None) -> bool:
    if not value:
        return False

    phone = value[1:] if value.startswith(PLUS_SIGN) else value
    if len(phone) > MAX_NUMBER_OF_DIGITS:
        return False
    if not phone:
        return False
    if not phone.isdigit() or phone[0] == "0":
        return False

    return CountryCode.parse_country_code(phone) is not None
