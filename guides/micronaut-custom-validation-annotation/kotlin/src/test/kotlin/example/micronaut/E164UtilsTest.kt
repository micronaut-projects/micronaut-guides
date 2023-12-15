package example.micronaut

import example.micronaut.E164Utils.isValid
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class E164UtilsTest {

    @ParameterizedTest
    @ValueSource(
        strings = [
            "+04630443322",
            "+1415555267102345",
            "+1-4155552671",
            ""]
    )
    fun invalidPhones(phone: String?) {
        assertFalse(isValid(phone))
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "+14155552671",
            "+442071838750",
            "+55115525632",
            "14155552671",
            "442071838750",
            "55115525632",
            "55115525632"]
    )
    fun validPhones(phone: String?) {
        assertTrue(isValid(phone))
    }
}