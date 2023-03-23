TRUNCATE
    bikeservicetest.public.users RESTART IDENTITY CASCADE ;
TRUNCATE
    bikeservicetest.public.user_role RESTART IDENTITY CASCADE;

INSERT INTO bikeservicetest.public.users(id, password, active, name)
VALUES (DEFAULT, '$2a$10$I/0KJRXs78nl5Z.Whzs9L.1uBwf9E/gJwqkZtgIZfrwarrOmJ.mBK', true, 'test'),
       (DEFAULT, '$2a$10$NJqlcPwFazO1CF4oWQcJuerCYhpYnSehN8HdgRWyq1mLx/5814cdy', true, 'pavel');

INSERT INTO bikeservicetest.public.user_role(user_id, status)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

