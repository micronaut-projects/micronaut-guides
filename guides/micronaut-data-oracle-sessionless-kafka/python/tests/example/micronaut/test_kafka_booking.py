import time

import pytest
import requests
from micronaut.transaction import TransactionOperations
from pyronaut.test import MicronautTest, micronaut_test_fixture

SEED = [
    "INSERT INTO flight_seat (id, status) VALUES ('MN123-12A', 'AVAILABLE')",
    "INSERT INTO flight_seat (id, status) VALUES ('MN123-12B', 'AVAILABLE')",
]
ROLLBACK_SEED = [
    "DELETE FROM booking WHERE seat_id IN ('MN123-12A', 'MN123-12B')",
    "DELETE FROM flight_seat WHERE id IN ('MN123-12A', 'MN123-12B')",
]


@pytest.fixture(scope="module")
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(environments=["test"], transactional=False))
    run_sql(fixture[TransactionOperations], SEED)
    yield fixture
    run_sql(fixture[TransactionOperations], ROLLBACK_SEED)
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_approved_payment_confirms_the_booking(client):
    seat_id = "MN123-12A"
    assert find_seat(client, seat_id)["status"] == "AVAILABLE"
    booking_id = book(client, seat_id, "demo-approve")
    await_confirmed(client, booking_id, seat_id)


def test_declined_payment_rolls_back_and_allows_the_same_seat_to_be_booked_again(client):
    seat_id = "MN123-12B"
    assert find_seat(client, seat_id)["status"] == "AVAILABLE"
    declined_booking_id = book(client, seat_id, "demo-decline")  # <1>
    approved_booking_id = book(client, seat_id, "demo-approve")  # <2>
    assert declined_booking_id != approved_booking_id
    await_confirmed(client, approved_booking_id, seat_id)  # <3>
    assert find_booking(client, declined_booking_id) is None  # <4>


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


def run_sql(transaction_operations, statements):
    def execute(status):
        connection = status.getConnection()
        for sql in statements:
            statement = connection.createStatement()
            try:
                statement.executeUpdate(sql)
            finally:
                statement.close()

    transaction_operations.executeWrite(execute)
