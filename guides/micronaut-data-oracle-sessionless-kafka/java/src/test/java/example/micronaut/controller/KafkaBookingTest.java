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
package example.micronaut.controller;

import example.micronaut.dto.BookingRequest;
import example.micronaut.dto.BookingView;
import example.micronaut.dto.FlightSeatView;
import example.micronaut.dto.PaymentCompleted;
import example.micronaut.entity.BookingStatus;
import example.micronaut.entity.FlightSeat;
import example.micronaut.entity.SeatStatus;
import example.micronaut.kafka.BookingClient;
import example.micronaut.repository.BookingRepository;
import example.micronaut.repository.FlightSeatRepository;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
class KafkaBookingTest {
    @Inject BookingRepository bookingRepository;
    @Inject FlightSeatRepository flightSeatRepository;
    @Inject BookingClient bookingClient;
    @Inject JsonMapper jsonMapper;
    @Inject @Client("/") HttpClient httpClient;
    @Value("${kafka.bootstrap.servers}") String bootstrapServers;

    @Test
    void controllerAndTwoConsumersCompleteTheSameBooking() {
        String seatId = "T" + UUID.randomUUID();
        List<Long> bookingIds = new ArrayList<>();
        flightSeatRepository.save(new FlightSeat(seatId, SeatStatus.AVAILABLE));
        try {
            Long bookingId = book(seatId, "demo-approve");
            bookingIds.add(bookingId);
            awaitConfirmed(bookingId, seatId);

            // Deliver a duplicate through Kafka, without calling the consumer directly.
            bookingClient.paymentCompleted(bookingId,
                new PaymentCompleted(bookingId, "already-completed", true, "duplicate"));
            awaitProcessedResult(bookingId, "already-completed");
            BookingView view = httpClient.toBlocking().retrieve("/bookings/" + bookingId, BookingView.class);
            assertEquals("demo-" + bookingId, view.paymentReference());
        } finally {
            cleanup(bookingIds, seatId);
        }
    }

    @Test
    void declinedPaymentRollsBackAndAllowsTheSameSeatToBeBookedAgain() {
        String seatId = "T" + UUID.randomUUID();
        List<Long> bookingIds = new ArrayList<>();
        flightSeatRepository.save(new FlightSeat(seatId, SeatStatus.AVAILABLE));
        try {
            Long declinedBookingId = book(seatId, "demo-decline");
            bookingIds.add(declinedBookingId);

            PaymentCompleted result = awaitProcessedResult(declinedBookingId, null); // <1>
            assertFalse(result.approved());
            assertTrue(bookingRepository.findById(declinedBookingId).isEmpty()); // <2>
            HttpClientResponseException missing = assertThrows(HttpClientResponseException.class,
                () -> httpClient.toBlocking().retrieve("/bookings/" + declinedBookingId, BookingView.class));
            assertEquals(HttpStatus.NOT_FOUND, missing.getStatus());
            FlightSeatView available = httpClient.toBlocking().retrieve("/bookings/seats/" + seatId, FlightSeatView.class);
            assertEquals(SeatStatus.AVAILABLE, available.status());

            Long approvedBookingId = book(seatId, "demo-approve"); // <3>
            bookingIds.add(approvedBookingId);
            assertNotEquals(declinedBookingId, approvedBookingId);
            awaitConfirmed(approvedBookingId, seatId); // <4>
            assertTrue(bookingRepository.findById(declinedBookingId).isEmpty());
        } finally {
            cleanup(bookingIds, seatId);
        }
    }

    private Long book(String seatId, String paymentToken) {
        var response = httpClient.toBlocking().exchange(
            HttpRequest.POST("/bookings", new BookingRequest(seatId, paymentToken)),
            BookingController.AcceptedBooking.class);
        assertEquals(HttpStatus.ACCEPTED, response.getStatus());
        Long bookingId = response.body().bookingId();
        assertNotNull(bookingId);
        assertTrue(bookingId > 0);
        return bookingId;
    }

    private void awaitConfirmed(Long bookingId, String seatId) {
        await().atMost(Duration.ofSeconds(60)).untilAsserted(() -> {
            var persisted = bookingRepository.findById(bookingId);
            assertTrue(persisted.isPresent(), "Booking has not committed yet");
            assertEquals(BookingStatus.CONFIRMED, persisted.orElseThrow().getStatus());
            assertEquals("demo-" + bookingId, persisted.orElseThrow().getPaymentReference());
            assertEquals(SeatStatus.BOOKED, flightSeatRepository.findById(seatId).orElseThrow().getStatus());
        });
        BookingView view = httpClient.toBlocking().retrieve("/bookings/" + bookingId, BookingView.class);
        assertEquals(bookingId, view.id());
        assertEquals(BookingStatus.CONFIRMED, view.status());
        FlightSeatView seat = httpClient.toBlocking().retrieve("/bookings/seats/" + seatId, FlightSeatView.class);
        assertEquals(SeatStatus.BOOKED, seat.status());
    }

    private PaymentCompleted awaitProcessedResult(Long bookingId, String transactionId) {
        Map<String, Object> config = Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG, "booking-test-" + UUID.randomUUID(),
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        try (KafkaConsumer<Long, String> observer = new KafkaConsumer<>(config, new LongDeserializer(), new StringDeserializer());
             AdminClient adminClient = AdminClient.create(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers))) {
            // A separate observer group sees results without stealing them from booking-completion.
            observer.subscribe(List.of("payment-results"));
            AtomicReference<ObservedResult> observed = new AtomicReference<>();
            await().atMost(Duration.ofSeconds(60)).until(() -> {
                for (var record : observer.poll(Duration.ofMillis(200))) {
                    PaymentCompleted payment = jsonMapper.readValue(record.value(), PaymentCompleted.class);
                    if (bookingId.equals(payment.bookingId()) &&
                        (transactionId == null || transactionId.equals(payment.transactionId()))) {
                        observed.set(new ObservedResult(new TopicPartition(record.topic(), record.partition()), record.offset(), payment));
                        return true;
                    }
                }
                return false;
            });
            ObservedResult record = observed.get();
            // SYNC_PER_RECORD commits the next offset after the completion listener returns.
            await().atMost(Duration.ofSeconds(60)).until(() -> {
                var offsets = adminClient.listConsumerGroupOffsets("booking-completion")
                    .partitionsToOffsetAndMetadata().get(5, TimeUnit.SECONDS);
                var committed = offsets.get(record.partition());
                return committed != null && committed.offset() > record.offset();
            });
            return record.payment();
        }
    }

    private void cleanup(List<Long> bookingIds, String seatId) {
        bookingIds.forEach(bookingRepository::deleteById);
        flightSeatRepository.deleteById(seatId);
    }

    private record ObservedResult(TopicPartition partition, long offset, PaymentCompleted payment) {
    }
}
