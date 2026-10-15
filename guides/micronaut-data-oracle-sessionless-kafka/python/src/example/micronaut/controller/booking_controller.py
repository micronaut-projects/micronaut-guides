from java.lang import Long
from dataclasses import dataclass
from typing import Annotated

from jakarta.validation import Valid
from micronaut.http import HttpResponse
from micronaut.http.annotation import Body, Controller, Get, Post
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.serde.annotation import Serdeable
from micronaut.transaction.jdbc.oracle import OracleSessionlessTransactionPropagationOperations

from ..dto.booking_request import BookingRequest
from ..dto.booking_view import BookingView
from ..dto.flight_seat_view import FlightSeatView
from ..dto.payment_requested import PaymentRequested
from ..kafka.booking_client import BookingClient
from ..repository.booking_repository import BookingRepository
from ..repository.flight_seat_repository import FlightSeatRepository
from ..service.booking_service import BookingService


@Serdeable  # <10>
@dataclass
class AcceptedBooking:
    bookingId: Long


@Controller("/bookings")  # <1>
@ExecuteOn(TaskExecutors.BLOCKING)  # <2>
class BookingController:
    def __init__(self, booking_client: BookingClient, booking_service: BookingService,
                 propagation: OracleSessionlessTransactionPropagationOperations,
                 booking_repository: BookingRepository, flight_seat_repository: FlightSeatRepository):
        self.booking_client = booking_client
        self.booking_service = booking_service
        self.propagation = propagation
        self.booking_repository = booking_repository
        self.flight_seat_repository = flight_seat_repository

    @Post  # <3>
    def book(self, request: Annotated[BookingRequest, Body, Valid]) -> HttpResponse[AcceptedBooking]:
        def reserve() -> PaymentRequested:
            booking = self.booking_service.reserve(request.seatId)  # <5>
            transaction_id = self.propagation.currentTransactionId().orElseThrow()  # <6>
            return PaymentRequested(booking.id, transaction_id, booking.amount, request.paymentToken)

        payment = self.propagation.withPropagation(reserve)  # <4>
        self.booking_client.requestPayment(payment.bookingId, payment)  # <7>
        return HttpResponse.accepted().body(AcceptedBooking(payment.bookingId))

    @Get("/{id}")  # <8>
    def find(self, id: Long) -> BookingView | None:
        booking = self.booking_repository.findById(id)
        return BookingView.from_booking(booking.get()) if booking.isPresent() else None

    @Get("/seats/{id}")  # <9>
    def seat(self, id: str) -> FlightSeatView | None:
        seat = self.flight_seat_repository.findById(id)
        return FlightSeatView.from_seat(seat.get()) if seat.isPresent() else None
