/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CountryCodeTest {

    @Test
    fun preferredNameGetsUsed() {
        val name = CountryCode.YEMEN.countryName
        assertEquals(name, "Yemen (Republic of)")
    }

    @Test
    fun defaultNameIsCapitalizedCorrectly() {
        val name = CountryCode.SPAIN.countryName
        assertEquals(name, "Spain")
    }

    @Test
    fun toStringReturnsCorrectValue() {
        val code = CountryCode.AMERICAN_SAMOA.toString()
        assertEquals(code, "1")
    }

    @Test
    fun countryCodeGetCodesReturnEveryCodeWithLongestCodesFirst() {
        assertTrue(CountryCode.CODES[0].length > 1)
    }

    @Test
    fun countryCodeParseCountryCodeParseCodes() {
        Assertions.assertNull(CountryCode.parseCountryCode("999999"))
        assertEquals("34", CountryCode.parseCountryCode("34630443322"))
        assertEquals("268", CountryCode.parseCountryCode("2684046441"))
        assertEquals("1", CountryCode.parseCountryCode("+14155552671"))
    }

    @Test
    fun countryCodeCountryCodesByCodeReturnAListOfCountryCodeWithTheSameCountryCode() {
        assertTrue(CountryCode.COUNTRYCODESBYCODE["999999"] == null)
        assertEquals(
            CountryCode.COUNTRYCODESBYCODE["1"], listOf(
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
                CountryCode.UNITED_STATES
            )
        )
    }
}