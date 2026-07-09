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
package example.micronaut;

import com.postmarkapp.postmark.client.data.model.message.Message;
import com.postmarkapp.postmark.client.data.model.message.MessageResponse;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.postmark.PostmarkEmailSender;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Requires(property = "spec.name", value = "MailControllerTest") // <1>
@Singleton
@Replaces(PostmarkEmailSender.class)
@Named(EmailSenderReplacement.NAME)
class EmailSenderReplacement implements AsyncTransactionalEmailSender<Message, MessageResponse> {

    public static final String NAME = "postmark";

    final List<Email> emails = new ArrayList<>();
    final List<Message> messages = new ArrayList<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Publisher<MessageResponse> sendAsync(@NotNull @Valid Email email,
                                         @NotNull Consumer<Message> emailRequest) throws EmailException {
        emails.add(email);
        Message message = new Message();
        emailRequest.accept(message);
        messages.add(message);
        MessageResponse response = new MessageResponse();
        response.setMessageId("00000000-0000-0000-0000-000000000000");
        return Mono.just(response);
    }

    public List<Email> getEmails() {
        return emails;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
