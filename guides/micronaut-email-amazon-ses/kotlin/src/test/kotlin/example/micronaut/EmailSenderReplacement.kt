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

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.email.AsyncTransactionalEmailSender
import io.micronaut.email.Email
import io.micronaut.email.EmailException
import io.micronaut.email.ses.AsyncSesEmailSender
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.ses.model.SendEmailResponse
import software.amazon.awssdk.services.ses.model.SesRequest
import software.amazon.awssdk.services.ses.model.SesResponse
import java.util.function.Consumer
import jakarta.validation.Valid

@Requires(property = "spec.name", value = "MailControllerTest") // <1>
@Singleton
@Replaces(AsyncSesEmailSender::class)
@Named(AsyncSesEmailSender.NAME)
open class EmailSenderReplacement : AsyncTransactionalEmailSender<SesRequest, SesResponse> {

    val emails = mutableListOf<Email>()

    override fun getName(): String = AsyncSesEmailSender.NAME

    @Throws(EmailException::class)
    override fun sendAsync(@Valid email: Email,
                           emailRequest: Consumer<SesRequest>): Publisher<SesResponse> {
        emails.add(email)
        return Mono.just(SendEmailResponse.builder().messageId("xxx-yyy-zzz").build())
    }
}
