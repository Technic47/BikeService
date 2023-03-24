TRUNCATE
    bikeservicetest.public.users RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.pictures RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.documents RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.user_role RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.user_item RESTART IDENTITY CASCADE;


INSERT INTO bikeservicetest.public.users(id, password, active, name)
VALUES (DEFAULT, '$2a$10$I/0KJRXs78nl5Z.Whzs9L.1uBwf9E/gJwqkZtgIZfrwarrOmJ.mBK', true, 'test'),
       (DEFAULT, '$2a$10$NJqlcPwFazO1CF4oWQcJuerCYhpYnSehN8HdgRWyq1mLx/5814cdy', true, 'pavel');

INSERT INTO bikeservicetest.public.user_role(user_id, status)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

INSERT INTO bikeservicetest.public.pictures(id, name)
VALUES (DEFAULT, 'test'),
       (DEFAULT, 'test2'),
       (DEFAULT, 'test3');

INSERT INTO bikeservicetest.public.documents(id, creator, description, link, name, picture, value)
VALUES (DEFAULT, 1, 'testDescription', 'testLink', 'testDoc1', 1, 'testValue'),
       (DEFAULT, 1, 'test', 'test', 'testDoc2', 1, 'test'),
       (DEFAULT, 2, 'test', 'test', 'testDoc3', 1, 'test'),
       (DEFAULT, 2, 'test', 'test', 'testDoc4', 1, 'test'),
       (DEFAULT, 2, 'test', 'test', 'testDoc5', 1, 'test');

INSERT INTO bikeservicetest.public.user_item(user_id, itemid, type)
VALUES (1, 1, 'Document'),
       (1, 2, 'Document'),
       (2, 3, 'Document'),
       (2, 4, 'Document'),
       (2, 5, 'Document');


