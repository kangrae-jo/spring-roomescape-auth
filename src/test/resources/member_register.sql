DELETE FROM reservation;
DELETE FROM store;
DELETE FROM auth_credential;
DELETE FROM member;
DELETE FROM reservation_time;
DELETE FROM theme;

ALTER TABLE reservation
    ALTER COLUMN id RESTART WITH 1;

ALTER TABLE reservation_time
    ALTER COLUMN id RESTART WITH 1;

ALTER TABLE theme
    ALTER COLUMN id RESTART WITH 1;

ALTER TABLE member
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO member (name, role)
VALUES ('milan', 'ADMIN'),
       ('guest', 'GUEST');

INSERT INTO auth_credential (member_id, password_hash)
VALUES (1, '$2y$10$BdKb4loIDkFnStCRLEo0muqSu/mGmTq63hZbG1O./dM9gtpfHBOny');

ALTER TABLE store
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO store (member_id)
VALUES (1);
