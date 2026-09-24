CREATE TABLE game (
    id CHAR(36) PRIMARY KEY,
    black_name VARCHAR(255) NOT NULL,
    white_name VARCHAR(255) NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP NOT NULL,
    draw BOOLEAN NOT NULL,
    winner CHAR(1)
);

CREATE TABLE game_state (
    id CHAR(36) PRIMARY KEY,
    game_id CHAR(36) NOT NULL,
    date_created TIMESTAMP NOT NULL,
    player CHAR(1) NOT NULL,
    fen VARCHAR(100) NOT NULL,
    pgn CLOB NOT NULL,
    move VARCHAR(10) NOT NULL,
    CONSTRAINT fk_game_state_game FOREIGN KEY (game_id) REFERENCES game(id)
);
