from micronaut.context.annotation import Property
from micronaut.configuration.kafka.annotation import KafkaListener, OffsetStrategy, Topic
from micronaut.transaction.jdbc.oracle import OracleSessionlessTransactionPropagationOperations

from ..dto.payment_completed import PaymentCompleted
from ..entity.booking_status import BookingStatus
from ..repository.booking_repository import BookingRepository
from ..service.booking_service import BookingService
from ..service.payment_declined_exception import PaymentDeclinedException


@KafkaListener(groupId="booking-completion", offsetStrategy=OffsetStrategy.SYNC_PER_RECORD,
               properties=[Property(name="auto.offset.reset", value="earliest"),
                           Property(name="enable.auto.commit", value="false")])
class CompletionConsumer:
    def __init__(self, booking_service: BookingService, booking_repository: BookingRepository,
                 propagation: OracleSessionlessTransactionPropagationOperations):
        self.booking_service = booking_service
        self.booking_repository = booking_repository
        self.propagation = propagation

    @Topic("payment-results")
    def complete(self, result: PaymentCompleted) -> None:
        booking = self.booking_repository.findById(result.bookingId)
        if booking.isPresent() and booking.get().status == BookingStatus.CONFIRMED:
            return  # <1>
        try:
            self.propagation.withPropagation(result.transactionId,  # <2>
                                            lambda: self.booking_service.complete(result))  # <3>
        except PaymentDeclinedException:  # <4>
            # The transactional interceptor has already rolled back the reservation.
            pass
