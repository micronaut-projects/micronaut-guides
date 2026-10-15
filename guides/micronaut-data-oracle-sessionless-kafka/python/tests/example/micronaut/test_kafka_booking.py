import time

import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.entity.flight_seat import FlightSeat
from example.micronaut.entity.seat_status import SeatStatus
from example.micronaut.repository.booking_repository import BookingRepository
from example.micronaut.repository.flight_seat_repository import FlightSeatRepository


@pytest.fixture(scope="module")
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(environments=["test"], transactional=False))  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    yield requests.with_context(my_context)
    reset_seats(my_context)  # <2>


def test_approved_payment_confirms_the_booking(client):
    seat_id = "MN123-12A"
    assert find_seat(client, seat_id)["status"] == "AVAILABLE"
    booking_id = book(client, seat_id, "demo-approve")
    await_confirmed(client, booking_id, seat_id)


def test_declined_payment_rolls_back_and_allows_the_same_seat_to_be_booked_again(client):
    seat_id = "MN123-12B"
    assert find_seat(client, seat_id)["status"] == "AVAILABLE"
    declined_booking_id = book(client, seat_id, "demo-decline")  # <3>
    approved_booking_id = book(client, seat_id, "demo-approve")  # <4>
    assert declined_booking_id != approved_booking_id
    await_confirmed(client, approved_booking_id, seat_id)  # <5>
    assert find_booking(client, declined_booking_id) is None  # <6>


def book(client, seat_id, payment_token):
    response = client.post("/bookings", json={"seatId": seat_id, "paymentToken": payment_token}, timeout=30)
    assert response.status_code == 202, response.text
    booking_id = response.json()["bookingId"]
    assert booking_id > 0
    return booking_id


def await_confirmed(client, booking_id, seat_id):
    deadline = time.monotonic() + 60
    while time.monotonic() < deadline:
        booking = find_booking(client, booking_id)
        if booking is not None and booking["status"] == "CONFIRMED":
            assert booking["id"] == booking_id
            assert booking["seatId"] == seat_id
            assert booking["paymentReference"] == f"demo-{booking_id}"
            assert find_seat(client, seat_id)["status"] == "BOOKED"
            return
        time.sleep(0.1)
    pytest.fail("Booking has not committed yet")


def find_booking(client, booking_id):
    response = client.get(f"/bookings/{booking_id}", timeout=30)
    if response.status_code == 404:
        return None
    assert response.status_code == 200, response.text
    return response.json()


def find_seat(client, seat_id):
    response = client.get(f"/bookings/seats/{seat_id}", timeout=30)
    assert response.status_code == 200, response.text
    return response.json()


def reset_seats(context):
    context[BookingRepository].deleteAll()
    seat_repository = context[FlightSeatRepository]
    seat_repository.update(FlightSeat("MN123-12A", SeatStatus.AVAILABLE))
    seat_repository.update(FlightSeat("MN123-12B", SeatStatus.AVAILABLE))
