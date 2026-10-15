from jakarta.inject import Singleton
from java.math import BigDecimal
from micronaut.transaction.annotation import OracleTransactional

from ..dto.payment_completed import PaymentCompleted
from ..entity.booking import Booking
from ..entity.booking_status import BookingStatus
from ..entity.seat_status import SeatStatus
from ..repository.booking_repository import BookingRepository
from ..repository.flight_seat_repository import FlightSeatRepository
from .payment_declined_exception import PaymentDeclinedException


@Singleton  # <9>
class BookingService:
    def __init__(self, flight_seat_repository: FlightSeatRepository,
                 booking_repository: BookingRepository):
        self.flight_seat_repository = flight_seat_repository
        self.booking_repository = booking_repository

    @OracleTransactional(sessionless=OracleTransactional.Sessionless.SUSPEND, timeout=300)  # <1>
    def reserve(self, seat_id: str) -> Booking:
        seat = self.flight_seat_repository.findByIdForUpdate(seat_id).orElseThrow()  # <2>
        if seat.status != SeatStatus.AVAILABLE:
            raise RuntimeError(f"Seat is unavailable: {seat_id}")
        seat.status = SeatStatus.HELD
        self.flight_seat_repository.update(seat)
        amount = BigDecimal("199.00")
        return self.booking_repository.save(Booking(seat_id, amount, BookingStatus.PENDING, "NONE"))  # <3>

    @OracleTransactional(sessionless=OracleTransactional.Sessionless.REQUIRES_SUSPENDED)  # <4>
    def complete(self, result: PaymentCompleted) -> None:
        booking = self.booking_repository.findByIdAndStatus(result.bookingId, BookingStatus.PENDING).orElseThrow()  # <5>
        if not result.approved:
            raise PaymentDeclinedException()  # <6>
        seat = self.flight_seat_repository.findByIdAndStatus(booking.seatId, SeatStatus.HELD).orElseThrow()  # <7>
        seat.status = SeatStatus.BOOKED
        self.flight_seat_repository.update(seat)
        booking.status = BookingStatus.CONFIRMED
        booking.paymentReference = result.paymentReference
        self.booking_repository.update(booking)  # <8>
