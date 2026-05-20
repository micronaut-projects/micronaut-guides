CREATE TABLE role (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    authority varchar(255) NOT NULL
);
CREATE TABLE "user" (
    id BIGSERIAL primary key NOT NULL,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    account_expired BOOLEAN NOT NULL,
    account_locked BOOLEAN NOT NULL,
    password_expired BOOLEAN NOT NULL
);
CREATE TABLE user_role(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role(id),
    FOREIGN KEY (user_id) REFERENCES "user"(id),
    PRIMARY KEY (role_id, user_id)
);