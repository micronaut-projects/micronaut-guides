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
package example.micronaut.kafka

import groovy.transform.CompileStatic

import example.micronaut.dto.PaymentCompleted
import example.micronaut.dto.PaymentRequested

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic
import org.apache.kafka.clients.producer.RecordMetadata

@CompileStatic
@KafkaClient(acks = KafkaClient.Acknowledge.ALL) // <1>
interface BookingClient {
    @Topic("payment-requests") // <2>
    RecordMetadata requestPayment(@KafkaKey Long bookingId, PaymentRequested request) // <3>

    @Topic("payment-results")
    RecordMetadata paymentCompleted(@KafkaKey Long bookingId, PaymentCompleted result)
}
