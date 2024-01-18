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

import io.micronaut.jms.annotations.JMSListener
import io.micronaut.jms.annotations.Queue
import io.micronaut.jms.sqs.configuration.SqsConfiguration.CONNECTION_FACTORY_BEAN_NAME
import io.micronaut.messaging.annotation.MessageBody
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

@JMSListener(CONNECTION_FACTORY_BEAN_NAME) // <1>
class DemoConsumer {
    private val messageCount = AtomicInteger(0)

    @Queue(value = "demo_queue") // <2>
    fun receive(@MessageBody body: String?) {  // <3>
        LOG.info("Message has been consumed. Message body: {}", body)
        messageCount.incrementAndGet()
    }

    fun getMessageCount(): Int {
        return messageCount.toInt()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DemoConsumer::class.java)
    }
}