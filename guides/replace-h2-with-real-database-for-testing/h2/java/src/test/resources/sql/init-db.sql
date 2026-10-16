CREATE TABLE IF NOT EXISTS products
(
    id   int          not null,
    code varchar(255) not null,
    name varchar(255) not null,
    primary key (id),
    unique (code)
);
