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

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.jms.annotations.JMSListener
import io.micronaut.jms.annotations.Queue
import io.micronaut.messaging.annotation.MessageBody

import java.util.concurrent.atomic.AtomicInteger

import static io.micronaut.jms.sqs.configuration.SqsConfiguration.CONNECTION_FACTORY_BEAN_NAME

@CompileStatic
@Slf4j('LOGGER')
@JMSListener(CONNECTION_FACTORY_BEAN_NAME)  // <1>
class DemoConsumer {

    private final AtomicInteger messageCount = new AtomicInteger(0);

    @Queue(value = "demo_queue", concurrency = "1-3")  // <2>
    void receive(@MessageBody String body) {  // <3>
        LOGGER.info("Message consumed: {}", body);
        messageCount.incrementAndGet()
    }

    int getMessageCount() {
        messageCount.intValue();
    }
}
