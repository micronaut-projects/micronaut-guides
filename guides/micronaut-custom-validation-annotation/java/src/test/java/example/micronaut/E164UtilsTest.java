package example.micronaut;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class E164UtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "+04630443322",
            "+1415555267102345",
            "+1-4155552671",
            ""
    })
    void invalidPhones(String phone) {
        assertFalse(E164Utils.isValid(phone));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "+14155552671",
            "+442071838750",
            "+55115525632",
            "14155552671",
            "442071838750",
            "55115525632",
            "55115525632",
    })
    void validPhones(String phone) {
        assertTrue(E164Utils.isValid(phone));
    }
}
