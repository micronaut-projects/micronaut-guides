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

import com.sendgrid.Request
import com.sendgrid.Response
import io.micronaut.email.AsyncEmailSender
import io.micronaut.email.BodyType.HTML
import io.micronaut.email.Email
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

@Controller("/mail") // <1>
class MailController(private val emailSender: AsyncEmailSender<Request, Response>) { // <2>

    @Post("/send") // <3>
    fun send(@Body("to") to: String): Publisher<HttpResponse<*>> { // <4>
        return Mono.from(emailSender.sendAsync(Email.builder()
                .to(to)
                .subject("Sending email with Twilio Sendgrid is Fun")
                .body("and <em>easy</em> to do anywhere with <strong>Micronaut Email</strong>", HTML)))
                .doOnNext { rsp: Response ->
                    LOG.info("response status {}\nresponse body {}\nresponse headers {}",
                            rsp.statusCode, rsp.body, rsp.headers)
                }.map { rsp: Response ->
                    if (rsp.statusCode >= 400) HttpResponse.unprocessableEntity() else HttpResponse.accepted<Any>()
                } // <5>
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(MailController::class.java)
    }
}
