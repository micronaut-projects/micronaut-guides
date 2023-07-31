CREATE TYPE measurement_unit as ENUM('Liter', 'Gram', 'Count');
CREATE cast ( character varying as measurement_unit) WITH inout AS assignment;

CREATE TABLE recipe ( /* <1> */
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    content TEXT NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP NOT NULL
);

CREATE TABLE ingredient ( /* <2> */
    id SERIAL PRIMARY KEY,
    recipe_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    amount FLOAT NOT NULL,
    unit measurement_unit NOT NULL,
    FOREIGN KEY (recipe_id) REFERENCES recipe(id)
);
