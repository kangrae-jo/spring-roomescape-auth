DELETE FROM auth_credential;
DELETE FROM member;

INSERT INTO member (id, name)
VALUES (1, 'milan');

INSERT INTO auth_credential (id, member_id, password_hash)
VALUES (1, 1, '$2y$10$BdKb4loIDkFnStCRLEo0muqSu/mGmTq63hZbG1O./dM9gtpfHBOny');
