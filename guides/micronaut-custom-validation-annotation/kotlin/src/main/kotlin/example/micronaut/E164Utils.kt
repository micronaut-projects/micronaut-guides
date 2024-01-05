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
        return CountryCode.parseCountryCode(phone) != null
    }
}

