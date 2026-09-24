from enum import Enum


class CountryCode(Enum):
    AMERICAN_SAMOA = ("1", "American Samoa")
    ANGUILLA = ("1", "Anguilla")
    ANTIGUA_AND_BARBUDA = ("1", "Antigua and Barbuda")
    BAHAMAS = ("1", "Bahamas (Commonwealth of the)")
    BARBADOS = ("1", "Barbados")
    BERMUDA = ("1", "Bermuda")
    BRAZIL = ("55", "Brazil (Federative Republic of)")
    BRITISH_VIRGIN_ISLANDS = ("1", "British Version Islands")
    CANADA = ("1", "Canada")
    CAYMAN_ISLANDS = ("1", "Cayman Islands")
    DOMINICA = ("1", "Dominica (Commonwealth of)")
    DOMINICAN_REPUBLIC = ("1", "Dominican Republic")
    GRENADA = ("1", "Grenada")
    GUAM = ("1", "Guam")
    JAMAICA = ("1", "Jamaica")
    MONTSERRAT = ("1", "Montserrat")
    NORTHERN_MARIANA_ISLANDS = ("1", "Northern Mariana Islands (Commonwealth of the)")
    PUERTO_RICO = ("1", "Puerto Rico")
    SAINT_KITTS_AND_NEVIS = ("1", "Saint Kitts and Nevis")
    SAINT_LUCIA = ("1", "Saint Lucia")
    SAINT_VINCENT_AND_THE_GRENADINES = ("1", "Saint Vincent and the Grenadines")
    SINT_MAARTEN = ("1", "Sint Maarten (Dutch part)")
    SPAIN = ("34", "Spain")
    SWAZILAND = ("268", "Swaziland (Kingdom of)")
    TRINIDAD_AND_TOBAGO = ("1", "Trinidad and Tobago")
    TURKS_AND_CAICOS_ISLANDS = ("1", "Turks and Caicos Islands")
    UNITED_KINGDOM = ("44", "United Kingdom of Great Britain and Northern Ireland")
    UNITED_STATES = ("1", "United States of America")
    YEMEN = ("967", "Yemen (Republic of)")

    def __init__(self, code: str, country_name: str):
        self.code = code
        self.country_name = country_name

    def __str__(self) -> str:
        return self.code

    @classmethod
    def country_codes_by_code(cls, code: str) -> list["CountryCode"]:
        return list(_COUNTRY_CODES_BY_CODE.get(code, ()))

    @classmethod
    def get_codes(cls) -> list[str]:
        return _CODES

    @classmethod
    def parse_country_code(cls, number: str) -> str | None:
        phone = number[1:] if number.startswith("+") else number
        for code in cls.get_codes():
            if phone.startswith(code):
                return code
        return None


_COUNTRY_CODES_BY_CODE: dict[str, list[CountryCode]] = {}
for country_code in CountryCode:
    _COUNTRY_CODES_BY_CODE.setdefault(country_code.code, []).append(country_code)

_CODES = sorted(_COUNTRY_CODES_BY_CODE, key=len, reverse=True)
