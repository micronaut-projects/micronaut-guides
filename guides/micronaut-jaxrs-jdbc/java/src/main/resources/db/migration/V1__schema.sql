DROP TABLE IF EXISTS pet;

CREATE TABLE pet (
                     id   BIGINT NOT NULL AUTO_INCREMENT UNIQUE PRIMARY KEY,
                     name  VARCHAR(255) NOT NULL UNIQUE,
                     type varchar(255) check (type in ('DOG', 'CAT'))
);
