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
package example.micronaut.kafka;

import example.micronaut.dto.PaymentCompleted;
import example.micronaut.entity.BookingStatus;
import example.micronaut.repository.BookingRepository;
import example.micronaut.service.BookingService;
import example.micronaut.service.PaymentDeclinedException;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetStrategy;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.transaction.jdbc.oracle.OracleSessionlessTransactionPropagationOperations;

@KafkaListener(groupId = "booking-completion", offsetStrategy = OffsetStrategy.SYNC_PER_RECORD) // <1>
public class CompletionConsumer {
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final OracleSessionlessTransactionPropagationOperations propagation;

    public CompletionConsumer(BookingService bookingService, BookingRepository bookingRepository,
                              OracleSessionlessTransactionPropagationOperations propagation) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
        this.propagation = propagation;
    }

    @Topic("payment-results") // <2>
    public void complete(PaymentCompleted result) {
        if (bookingRepository.findById(result.bookingId()).filter(b -> b.status() == BookingStatus.CONFIRMED).isPresent()) {
            return; // <3>
        }
        try {
            propagation.withPropagation(result.transactionId(), () -> { // <4>
                bookingService.complete(result); // <5>
                return null;
            });
        } catch (PaymentDeclinedException expected) { // <6>
            // The transactional interceptor has already rolled back the reservation.
        }
    }
}
