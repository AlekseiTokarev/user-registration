
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    user_id    serial
        CONSTRAINT user_pk
            PRIMARY KEY,
    first_name varchar              NOT NULL,
    last_name  varchar              NOT NULL,
    email      varchar,
    active     boolean DEFAULT TRUE NOT NULL
);

ALTER TABLE users
    OWNER TO varo;

CREATE INDEX idx_email
    ON users (email);

CREATE TABLE address
(
    address_id serial
        CONSTRAINT address_pk
            PRIMARY KEY,
    user_id    integer               NOT NULL
        CONSTRAINT fk_user_id
            REFERENCES users,
    line1      varchar               NOT NULL,
    line2      varchar,
    city       varchar               NOT NULL,
    state      varchar               NOT NULL,
    zip        varchar               NOT NULL,
    archived   boolean DEFAULT FALSE NOT NULL
);

ALTER TABLE address
    OWNER TO varo;

CREATE INDEX "idx_userId"
    ON address (user_id);
