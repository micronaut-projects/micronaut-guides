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

import com.sendgrid.Request;
import com.sendgrid.Response;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.email.AsyncEmailSender;
import io.micronaut.email.AsyncTransactionalEmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static io.micronaut.http.HttpStatus.ACCEPTED;

@Requires(property = "spec.name", value = "MailControllerTest") // <1>
@Singleton
@Replaces(AsyncEmailSender.class)
@Named(EmailSenderReplacement.NAME)
class EmailSenderReplacement implements AsyncTransactionalEmailSender<Request, Response> {

    public static final String NAME = "sendgrid";

    final List<Email> emails = new ArrayList<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Publisher<Response> sendAsync(@NotNull @Valid Email email,
                                         @NotNull Consumer<Request> emailRequest) throws EmailException {
        emails.add(email);
        Response response = new Response();
        response.setStatusCode(ACCEPTED.getCode());
        return Mono.just(response);
    }

    public List<Email> getEmails() {
        return emails;
    }
}
