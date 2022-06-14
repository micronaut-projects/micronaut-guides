CREATE TABLE book (
    id  BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    availability  ENUM('available', 'reserved', 'not available') NOT NULL,
    author  VARCHAR(255),
    ISBN  CHAR(13)
);

INSERT INTO book
    (name, availability, author, ISBN)
VALUES
    ("Alice's Adventures in Wonderland",      "available",   "Lewis Caroll",   "9783161484100"),
    ("The Hitchhiker's Guide to the Galaxy",  "reserved",    "Douglas Adams",  NULL),
    ("Java Guide for Beginners",              "available",   NULL,             NULL);