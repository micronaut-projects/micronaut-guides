CREATE TYPE bookavailability as ENUM('available', 'reserved', 'not available');
CREATE cast ( character varying as bookavailability) WITH inout AS assignment;
CREATE TABLE book (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    availability bookavailability NOT NULL,
    author VARCHAR(255),
    ISBN CHAR(13)
);

INSERT INTO
    book(name, availability, author, ISBN)
VALUES
    ('Alice''s Adventures in Wonderland',      'available',   'Lewis Caroll',   '9783161484100'),
    ('The Hitchhiker''s Guide to the Galaxy',  'reserved',    'Douglas Adams',  NULL),
    ('Java Guide for Beginners',               'available',   NULL,             NULL);
