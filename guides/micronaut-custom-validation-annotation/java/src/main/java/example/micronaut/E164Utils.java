package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;

/**
 * Utility methods to ease {@link E164} validation.
 */
public final class E164Utils {
    private static final int MAX_NUMBER_OF_DIGITS = 15;
    private static final String PLUS_SIGN = "+";

    private E164Utils() {
    }

    /**
     * @param value phone number
     * @return Whether a phone is E.164 formatted
     */
    public static boolean isValid(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        String phone = value.startsWith(PLUS_SIGN) ? value.substring(1) : value;
        if (phone.length() > MAX_NUMBER_OF_DIGITS) {
            return false;
        }
        if (phone.isEmpty()) {
            return false;
        }
        if (!StringUtils.isDigits(phone) || phone.charAt(0) == '0') {
            return false;
        }

        return CountryCode.parseCountryCode(phone) != null;
    }
}
