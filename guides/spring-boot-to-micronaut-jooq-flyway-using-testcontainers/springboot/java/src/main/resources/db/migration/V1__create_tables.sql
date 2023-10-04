create table users
(
    id         bigserial not null,
    name       varchar   not null,
    email      varchar   not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id),
    constraint user_email_unique unique (email)
);

create table posts
(
    id         bigserial                    not null,
    title      varchar                      not null,
    content    varchar                      not null,
    created_by bigint references users (id) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);

create table comments
(
    id         bigserial                    not null,
    name       varchar                      not null,
    content    varchar                      not null,
    post_id    bigint references posts (id) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);

ALTER SEQUENCE users_id_seq RESTART WITH 101;
ALTER SEQUENCE posts_id_seq RESTART WITH 101;
ALTER SEQUENCE comments_id_seq RESTART WITH 101;
