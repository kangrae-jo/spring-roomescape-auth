CREATE TABLE IF NOT EXISTS reservation_time
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    start_at TIME   NOT NULL UNIQUE,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS theme
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    name          VARCHAR(255) NOT NULL,
    description   VARCHAR(255) NOT NULL,
    thumbnail_url VARCHAR(255) NOT NULL,
    runtime       BIGINT       NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS member
(
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS auth_credential
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    member_id     BIGINT       NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE IF NOT EXISTS store
(
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    member_id   BIGINT      NOT NULL, -- manager

    PRIMARY KEY (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
    );

CREATE TABLE IF NOT EXISTS reservation
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255) NOT NULL,
    date     DATE         NOT NULL,
    time_id  BIGINT       NOT NULL,
    theme_id BIGINT       NOT NULL,
    store_id BIGINT       NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT unique_reservation_store_date_time_theme
        UNIQUE (store_id, date, theme_id, time_id),
    FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (store_id) REFERENCES store (id)
);
