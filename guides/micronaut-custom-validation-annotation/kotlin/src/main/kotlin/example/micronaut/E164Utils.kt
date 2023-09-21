package example.micronaut

import io.micronaut.core.util.StringUtils

object E164Utils {
    private const val MAX_NUMBER_OF_DIGITS = 15
    private const val PLUS_SIGN = "+"

    fun isValid(value: String?): Boolean {
        if (value.isNullOrEmpty()) {
            return false
        }
        val phone = if (value.startsWith(PLUS_SIGN)) value.substring(1) else value
        if (phone.length > MAX_NUMBER_OF_DIGITS) {
            return false
        }
        if (phone.isEmpty()) {
            return false
        }
        if (!StringUtils.isDigits(phone) || phone[0] == '0') {
            return false
        }
        return CountryCode.parseCountryCode(phone) != null;
    }
}

