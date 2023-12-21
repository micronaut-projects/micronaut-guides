/*
 * Copyright 2017-2023 original authors
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
import io.micronaut.email.AsyncTransactionalEmailSender
import io.micronaut.email.Email
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.email.BodyType.HTML
import static io.micronaut.http.HttpStatus.ACCEPTED

@Property(name = 'spec.name', value = 'MailControllerSpec') // <1>
@MicronautTest // <2>
class MailControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient httpClient // <3>

    @Inject
    BeanContext beanContext

    void getMailSendEndpointSendsAnEmail() {

        when:
        HttpResponse<?> response = httpClient.toBlocking().exchange(
                HttpRequest.POST('/mail/send', [to: 'johnsnow@micronaut.example']))

        then:
        ACCEPTED == response.status()

        when:
        AsyncTransactionalEmailSender<?, ?> sender = beanContext.getBean(AsyncTransactionalEmailSender)

        then:
        sender instanceof EmailSenderReplacement

        when:
        EmailSenderReplacement sendgridSender = (EmailSenderReplacement) sender

        then:
        1 == sendgridSender.emails.size()

        when:
        Email email = sendgridSender.emails[0]

        then:
        email.from.email == 'john@micronaut.example'
        email.to
        email.to.first()
        email.to.first().email == 'johnsnow@micronaut.example'
        email.subject == 'Sending email with Twilio Sendgrid is Fun'
        email.body
        email.body.get(HTML).present
        email.body.get(HTML).get() == 'and <em>easy</em> to do anywhere with <strong>Micronaut Email</strong>'
    }
}
