from example.micronaut.country_code import CountryCode


def test_preferred_name_gets_used():
    assert CountryCode.YEMEN.country_name == "Yemen (Republic of)"


def test_default_name_is_capitalized_correctly():
    assert CountryCode.SPAIN.country_name == "Spain"


def test_string_returns_correct_value():
    assert str(CountryCode.AMERICAN_SAMOA) == "1"


def test_get_codes_returns_every_code_with_longest_codes_first():
    assert len(CountryCode.get_codes()[0]) > 1


def test_parse_country_code_parses_codes():
    assert CountryCode.parse_country_code("999999") is None

    assert CountryCode.parse_country_code("34630443322") == "34"
    assert CountryCode.parse_country_code("2684046441") == "268"
    assert CountryCode.parse_country_code("+14155552671") == "1"


def test_country_codes_by_code_returns_list_with_the_same_country_code():
    assert CountryCode.country_codes_by_code("999999") == []
    assert CountryCode.country_codes_by_code("1") == [
        CountryCode.AMERICAN_SAMOA,
        CountryCode.ANGUILLA,
        CountryCode.ANTIGUA_AND_BARBUDA,
        CountryCode.BAHAMAS,
        CountryCode.BARBADOS,
        CountryCode.BERMUDA,
        CountryCode.BRITISH_VIRGIN_ISLANDS,
        CountryCode.CANADA,
        CountryCode.CAYMAN_ISLANDS,
        CountryCode.DOMINICA,
        CountryCode.DOMINICAN_REPUBLIC,
        CountryCode.GRENADA,
        CountryCode.GUAM,
        CountryCode.JAMAICA,
        CountryCode.MONTSERRAT,
        CountryCode.NORTHERN_MARIANA_ISLANDS,
        CountryCode.PUERTO_RICO,
        CountryCode.SAINT_KITTS_AND_NEVIS,
        CountryCode.SAINT_LUCIA,
        CountryCode.SAINT_VINCENT_AND_THE_GRENADINES,
        CountryCode.SINT_MAARTEN,
        CountryCode.TRINIDAD_AND_TOBAGO,
        CountryCode.TURKS_AND_CAICOS_ISLANDS,
        CountryCode.UNITED_STATES,
    ]
