DELETE FROM booking WHERE seat_id IN ('MN123-12A', 'MN123-12B');
UPDATE flight_seat SET status = 'AVAILABLE' WHERE id IN ('MN123-12A', 'MN123-12B');
