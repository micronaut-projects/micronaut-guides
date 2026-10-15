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
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.email.AsyncEmailSender
import io.micronaut.email.Email
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

import static io.micronaut.email.BodyType.HTML

@CompileStatic
@Slf4j
@Controller('/mail') // <1>
class MailController {

    private final AsyncEmailSender<Message, MessageResponse> emailSender

    MailController(AsyncEmailSender<Message, MessageResponse> emailSender) { // <2>
        this.emailSender = emailSender
    }

    @Post('/send') // <3>
    Publisher<HttpResponse<?>> send(@Body('to') String to) { // <4>
        Mono.from(emailSender.sendAsync(Email.builder()
                .to(to)
                .subject('Sending email with Postmark is Fun')
                .body('and <em>easy</em> to do anywhere with <strong>Micronaut Email</strong>', HTML), // <5>
                { Message message ->
                    message.tag = 'welcome'
                    message.addHeader('X-Customer-Type', 'trial')
                }))
                .doOnNext(rsp -> {
                    log.info('message id: {}', rsp.messageId)
                }).map(rsp -> HttpResponse.accepted()) // <6>
                as Publisher
    }
}
