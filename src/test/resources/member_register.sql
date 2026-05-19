DELETE FROM auth_credential;
DELETE FROM member;

INSERT INTO member (id, name)
VALUES (1, 'milan');

INSERT INTO auth_credential (id, member_id, password)
VALUES (1, 1, '1234');
