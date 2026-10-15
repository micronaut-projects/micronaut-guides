CREATE TABLE flight_seat (
    id VARCHAR2(40) PRIMARY KEY,
    status VARCHAR2(20) NOT NULL CHECK (status IN ('AVAILABLE', 'HELD', 'BOOKED'))
);
CREATE SEQUENCE booking_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE booking (
    id NUMBER(19) PRIMARY KEY,
    seat_id VARCHAR2(40) NOT NULL REFERENCES flight_seat(id),
    amount NUMBER(10, 2) NOT NULL,
    status VARCHAR2(20) NOT NULL CHECK (status IN ('PENDING', 'CONFIRMED')),
    payment_reference VARCHAR2(80) NOT NULL
);
