from java.lang import Long
from typing import Annotated

from micronaut.context.annotation import Property
from micronaut.configuration.kafka.annotation import KafkaClient, KafkaKey, Topic
from org.apache.kafka.clients.producer import RecordMetadata

from ..dto.payment_completed import PaymentCompleted
from ..dto.payment_requested import PaymentRequested


@KafkaClient(acks=-1,
             properties=[Property(name="enable.idempotence", value="true")])
class BookingClient:
    @Topic("payment-requests")
    def requestPayment(self, bookingId: Annotated[Long, KafkaKey],
                       request: PaymentRequested) -> RecordMetadata: ...

    @Topic("payment-results")
    def paymentCompleted(self, bookingId: Annotated[Long, KafkaKey],
                         result: PaymentCompleted) -> RecordMetadata: ...
