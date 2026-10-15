from micronaut.context.annotation import Property
from micronaut.configuration.kafka.annotation import KafkaListener, OffsetStrategy, Topic

from ..dto.payment_requested import PaymentRequested
from ..service.demo_payment_gateway import DemoPaymentGateway
from .booking_client import BookingClient


@KafkaListener(groupId="payment-processing", offsetStrategy=OffsetStrategy.SYNC_PER_RECORD,
               properties=[Property(name="auto.offset.reset", value="earliest"),
                           Property(name="enable.auto.commit", value="false")])  # <1>
class PaymentConsumer:
    def __init__(self, demo_payment_gateway: DemoPaymentGateway, booking_client: BookingClient):
        self.demo_payment_gateway = demo_payment_gateway
        self.booking_client = booking_client

    @Topic("payment-requests")  # <2>
    def pay(self, request: PaymentRequested) -> None:
        result = self.demo_payment_gateway.pay(request)  # <3>
        self.booking_client.paymentCompleted(request.bookingId, result)  # <4>
