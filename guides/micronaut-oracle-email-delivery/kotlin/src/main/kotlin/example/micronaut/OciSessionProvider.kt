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

import io.micronaut.context.annotation.Property
import io.micronaut.email.javamail.sender.MailPropertiesProvider
import io.micronaut.email.javamail.sender.SessionProvider
import jakarta.inject.Singleton
import java.util.Properties
import jakarta.mail.Authenticator
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session

@Singleton // <1>
class OciSessionProvider(provider: MailPropertiesProvider,
                         @Property(name = "smtp.user") user: String, // <2>
                         @Property(name = "smtp.password") password: String) // <2>
    : SessionProvider {

    private val properties: Properties
    private val user: String
    private val password: String

    init {
        properties = provider.mailProperties()
        this.user = user
        this.password = password
    }

    override fun session(): Session =
        Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication() = PasswordAuthentication(user, password) // <3>
        })
}
