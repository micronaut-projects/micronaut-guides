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

import example.micronaut.dto.PaymentCompleted
import example.micronaut.dto.PaymentRequested
import example.micronaut.service.DemoPaymentGateway
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetStrategy
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaListener(groupId = "payment-processing", offsetStrategy = OffsetStrategy.SYNC_PER_RECORD)
class PaymentConsumer(private val demoPaymentGateway: DemoPaymentGateway, private val bookingClient: BookingClient) {
    @Topic("payment-requests")
    fun pay(request: PaymentRequested) {
        val decision = demoPaymentGateway.pay(request) // <1>
        bookingClient.paymentCompleted(request.bookingId, PaymentCompleted(
            request.bookingId, request.transactionId, decision.approved, decision.reference
        )) // <2>
    }
}
