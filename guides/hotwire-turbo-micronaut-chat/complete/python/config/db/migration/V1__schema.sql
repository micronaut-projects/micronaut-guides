DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS message;

CREATE TABLE room (
    id   BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    name  VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE message (
    id   BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
    content  VARCHAR(255) NOT NULL,
    room_id BIGINT,
    date_created datetime NULL,
    INDEX r_id (room_id),
    FOREIGN KEY (room_id)
        REFERENCES room(id)
        ON DELETE CASCADE
);