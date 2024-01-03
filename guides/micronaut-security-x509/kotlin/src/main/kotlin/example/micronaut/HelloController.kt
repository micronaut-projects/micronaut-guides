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

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS
import io.micronaut.security.x509.X509Authentication

@Controller
class HelloController {

    @Secured(IS_ANONYMOUS) // <1>
    @Get(produces = [TEXT_PLAIN]) // <2>
    fun hello(x509Authentication: X509Authentication?,  // <3>
              authentication: Authentication?): String { // <4>
        if (x509Authentication == null && authentication == null) {
            return "Hello unknown!" // <5>
        }
        if (x509Authentication == null) {
            return "ERROR: Authentication is present but not X509Authentication" // <6>
        }
        return if (x509Authentication !== authentication) {
            "ERROR: Authentication and X509Authentication should be the same instance" // <7>
        } else "Hello ${x509Authentication.name} (X.509 cert issued by ${x509Authentication.certificate.issuerX500Principal.name})" // <8>

    }
}
