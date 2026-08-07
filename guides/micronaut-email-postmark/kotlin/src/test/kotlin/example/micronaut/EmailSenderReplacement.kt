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

import com.postmarkapp.postmark.client.data.model.message.Message
import com.postmarkapp.postmark.client.data.model.message.MessageResponse
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.email.AsyncTransactionalEmailSender
import io.micronaut.email.Email
import io.micronaut.email.EmailException
import io.micronaut.email.postmark.PostmarkEmailSender
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import java.util.function.Consumer
import jakarta.validation.Valid

@Requires(property = "spec.name", value = "MailControllerTest") // <1>
@Singleton
@Replaces(PostmarkEmailSender::class)
@Named(EmailSenderReplacement.NAME)
open class EmailSenderReplacement : AsyncTransactionalEmailSender<Message, MessageResponse> {

    val emails = mutableListOf<Email>()
    val messages = mutableListOf<Message>()

    override fun getName(): String = NAME

    @Throws(EmailException::class)
    override fun sendAsync(@Valid email: Email,
                           emailRequest: Consumer<Message>): Publisher<MessageResponse> {
        emails.add(email)
        val message = Message()
        emailRequest.accept(message)
        messages.add(message)
        val response = MessageResponse()
        response.messageId = "00000000-0000-0000-0000-000000000000"
        return Mono.just(response)
    }

    companion object {
        const val NAME = "postmark"
    }
}
