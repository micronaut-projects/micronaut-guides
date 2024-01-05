/*
 * Copyright 2017-2024 original authors
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
