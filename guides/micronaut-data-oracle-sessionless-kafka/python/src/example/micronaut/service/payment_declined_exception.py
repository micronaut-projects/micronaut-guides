class PaymentDeclinedException(RuntimeError):
    def __init__(self):
        super().__init__("Payment declined; roll back the seat reservation")
