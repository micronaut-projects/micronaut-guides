from threading import Lock

from jakarta.inject import Singleton

from ..dto.payment_completed import PaymentCompleted
from ..dto.payment_requested import PaymentRequested


@Singleton
class DemoPaymentGateway:
    def __init__(self):
        self.decisions = {}
        self.lock = Lock()

    def pay(self, request: PaymentRequested) -> PaymentCompleted:
        with self.lock:
            if request.bookingId not in self.decisions:
                approved = request.paymentToken == "demo-approve"
                self.decisions[request.bookingId] = PaymentCompleted(
                    request.bookingId, request.transactionId, approved,
                    f"demo-{request.bookingId}" if approved else "DECLINED")
            return self.decisions[request.bookingId]
