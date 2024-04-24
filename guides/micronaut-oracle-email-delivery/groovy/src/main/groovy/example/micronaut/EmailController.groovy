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

import groovy.transform.CompileStatic
import io.micronaut.email.Attachment
import io.micronaut.email.Email
import io.micronaut.email.EmailSender
import io.micronaut.email.template.TemplateBody
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.views.ModelAndView
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.http.annotation.Produces
import io.micronaut.http.annotation.Consumes
import java.time.LocalDateTime

import static io.micronaut.email.BodyType.HTML
import static io.micronaut.http.MediaType.APPLICATION_OCTET_STREAM_TYPE
import static io.micronaut.http.MediaType.MULTIPART_FORM_DATA
import static io.micronaut.http.MediaType.TEXT_PLAIN

@CompileStatic
@ExecuteOn(TaskExecutors.BLOCKING) // <1>
@Controller('/email') // <2>
class EmailController {

    private final EmailSender<?, ?> emailSender

    EmailController(EmailSender<?, ?> emailSender) { // <3>
        this.emailSender = emailSender
    }

    @Produces(TEXT_PLAIN) // <4>
    @Post('/basic')
    String index() {
        emailSender.send(Email.builder()
                .to('basic@domain.com')
                .subject('Micronaut Email Basic Test: ' + LocalDateTime.now())
                .body('Basic email')) // <5>
        return 'Email sent.'
    }

    @Produces(TEXT_PLAIN) // <4>
    @Post('/template/{name}')
    String template(String name) {
        emailSender.send(Email.builder()
                .to('template@domain.com')
                .subject('Micronaut Email Template Test: ' + LocalDateTime.now())
                .body(new TemplateBody<>(HTML,
                        new ModelAndView<>('email', [name: name]))))  // <6>
        return 'Email sent.'
    }

    @Consumes(MULTIPART_FORM_DATA) // <7>
    @Produces(TEXT_PLAIN) // <4>
    @Post('/attachment')
    String attachment(CompletedFileUpload file) throws IOException {

        String contentType = file.contentType.orElse(APPLICATION_OCTET_STREAM_TYPE)

        emailSender.send(Email.builder()
                .to('attachment@domain.com')
                .subject('Micronaut Email Attachment Test: ' + LocalDateTime.now())
                .body('Attachment email')
                .attachment(Attachment.builder()
                        .filename(file.filename)
                        .contentType(contentType)
                        .content(file.bytes)
                        .build()
                )) // <8>
        return 'Email sent.'
    }
}
