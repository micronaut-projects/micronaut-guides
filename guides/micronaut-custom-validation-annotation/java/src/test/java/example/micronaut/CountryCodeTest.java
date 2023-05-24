package example.micronaut;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountryCodeTest {
    @Test
    void preferredNameGetsUsed() {
        String name = CountryCode.YEMEN.getCountryName();
        assertEquals(name, "Yemen (Republic of)");
    }

    @Test
    void defaultNameIsCapitalizedCorrectly() {
        String name = CountryCode.SPAIN.getCountryName();
        assertEquals(name, "Spain");
    }

    @Test
    void toStringReturnsCorrectValue() {
        String code = CountryCode.AMERICAN_SAMOA.toString();
        assertEquals(code, "1");
    }

    @Test
    void countryCodeGetCodesReturnEveryCodeWithLongestCodesFirst() {
        assertTrue(CountryCode.getCodes().get(0).length() > 1);
    }

    @Test
    void countryCodeParseCountryCodeParseCodes() {

        assertNull(CountryCode.parseCountryCode("999999"));

        assertEquals("34",CountryCode.parseCountryCode("34630443322"));
        assertEquals("268",CountryCode.parseCountryCode("2684046441"));
        assertEquals("1",CountryCode.parseCountryCode("+14155552671"));
    }

    @Test
    void countryCodeCountryCodesByCodeReturnAListOfCountryCodeWithTheSameCountryCode() {
        assertTrue(CountryCode.countryCodesByCode("999999").isEmpty());
        assertEquals(CountryCode.countryCodesByCode("1"), Arrays.asList(
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
                CountryCode.UNITED_STATES));
    }
}
