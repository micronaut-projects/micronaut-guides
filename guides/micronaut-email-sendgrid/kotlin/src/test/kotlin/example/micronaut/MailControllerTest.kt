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

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Property
import io.micronaut.core.util.CollectionUtils
import io.micronaut.email.AsyncTransactionalEmailSender
import io.micronaut.email.BodyType.HTML
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.ACCEPTED
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Property(name = "spec.name", value = "MailControllerTest") // <1>
@MicronautTest // <2>
class MailControllerTest(@Client("/") val httpClient: HttpClient) { // <3>

    @Inject
    lateinit var beanContext: BeanContext

    @Test
    fun mailSendEndpointSendsAnEmail() {

        val response: HttpResponse<*> = httpClient.toBlocking().exchange<Map<String, String>, Any>(
                HttpRequest.POST("/mail/send", mapOf("to" to "johnsnow@micronaut.example")))
        assertEquals(ACCEPTED, response.status())

        val sender = beanContext.getBean(AsyncTransactionalEmailSender::class.java)
        assertTrue(sender is EmailSenderReplacement)

        val sendgridSender = sender as EmailSenderReplacement
        assertTrue(CollectionUtils.isNotEmpty(sendgridSender.emails as Collection<*>?))
        assertEquals(1, sendgridSender.emails.size)

        val email = sendgridSender.emails[0]
        assertEquals(email.from.email, "john@micronaut.example")
        assertNotNull(email.to)
        assertTrue(email.to!!.stream().findFirst().isPresent)
        assertEquals(email.to!!.stream().findFirst().get().email, "johnsnow@micronaut.example")
        assertEquals(email.subject, "Sending email with Twilio Sendgrid is Fun")
        assertNotNull(email.body)
        assertTrue(email.body!![HTML].isPresent)
        assertEquals(email.body!![HTML].get(), "and <em>easy</em> to do anywhere with <strong>Micronaut Email</strong>")
    }
}
