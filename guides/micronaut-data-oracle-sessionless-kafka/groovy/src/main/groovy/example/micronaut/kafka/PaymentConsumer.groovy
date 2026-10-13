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
import example.micronaut.service.DemoPaymentGateway

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetStrategy
import io.micronaut.configuration.kafka.annotation.Topic

@CompileStatic
@KafkaListener(groupId = "payment-processing", offsetStrategy = OffsetStrategy.SYNC_PER_RECORD)
class PaymentConsumer {
    private final DemoPaymentGateway demoPaymentGateway
    private final BookingClient bookingClient

    PaymentConsumer(DemoPaymentGateway demoPaymentGateway, BookingClient bookingClient) {
        this.demoPaymentGateway = demoPaymentGateway
        this.bookingClient = bookingClient
    }

    @Topic("payment-requests")
    void pay(PaymentRequested request) {
        DemoPaymentGateway.Decision decision = demoPaymentGateway.pay(request) // <1>
        bookingClient.paymentCompleted(request.bookingId, new PaymentCompleted(
            request.bookingId, request.transactionId, decision.approved, decision.reference)) // <2>
    }
}
