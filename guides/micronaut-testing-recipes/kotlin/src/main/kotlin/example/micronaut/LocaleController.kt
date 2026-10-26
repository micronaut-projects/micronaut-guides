/*
 * Copyright 2017-2026 original authors
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

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.cookie.Cookie
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import jakarta.validation.constraints.NotBlank
import java.net.URI

@Controller("/locale") // <1>
open class LocaleController {

    @Secured(SecurityRule.IS_ANONYMOUS) // <2>
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // <3>
    @Post // <4>
    open fun changeLocale(@Body("locale") @NotBlank locale: String): HttpResponse<*> =
        HttpResponse.seeOther<Any>(HOME)
            .cookie(Cookie.of(COOKIE_NAME, locale)) // <5>

    companion object {
        private val HOME = URI.create("/")
        private const val COOKIE_NAME = "locale"
    }
}
