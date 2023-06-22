TRUNCATE
    bikeservicetest.public.users RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.pictures RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.documents RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.consumables RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.fasteners RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.manufacturers RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.tools RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.parts RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.part_item RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.user_role RESTART IDENTITY CASCADE;
TRUNCATE
    bikeservicetest.public.user_item RESTART IDENTITY CASCADE;

INSERT INTO bikeservicetest.public.pictures(id, name)
VALUES (DEFAULT, 'test'),
       (DEFAULT, 'test2'),
       (DEFAULT, 'test3');

INSERT INTO bikeservicetest.public.documents(id, creator, description, link, name, picture, value, is_shared)
VALUES (DEFAULT, 1, 'testDescription', 'testLink', 'testDoc1', 1, 'testValue', false),
       (DEFAULT, 1, 'test', 'test', 'testDoc2', 1, 'test', false),
       (DEFAULT, 2, 'test', 'test', 'testDoc3', 1, 'test', false),
       (DEFAULT, 2, 'test', 'test', 'testDoc4', 1, 'test', false),
       (DEFAULT, 2, 'test', 'test', 'testDoc5', 1, 'test', false);

INSERT INTO bikeservicetest.public.fasteners(id, creator, description, link, name, picture, value, is_shared)
VALUES (DEFAULT, 1, 'testDescription', 'testLink', 'testFast1', 1, 'testValue', false);

INSERT INTO bikeservicetest.public.consumables(id, creator, description, link, name, picture, value, is_shared)
VALUES (DEFAULT, 1, 'testDescription', 'testLink', 'testCons1', 1, 'testValue', false);

INSERT INTO bikeservicetest.public.tools(id, creator, description, link, name, picture, value, is_shared)
VALUES (DEFAULT, 1, 'testDescription', 'testLink', 'testTool1', 1, 'testValue', false);

INSERT INTO bikeservicetest.public.manufacturers(id, creator, description, link, name, picture, value, is_shared)
VALUES (DEFAULT, 1, 'testDescription', 'testLink', 'testManufacture', 1, 'testValue', false);

INSERT INTO bikeservicetest.public.parts(id, creator, description, link, name, picture, value, manufacturer, model, is_shared)
VALUES (DEFAULT, 1, 'test', 'test', 'testPart1', 1, 'test', 1, 'testModel', false),
       (DEFAULT, 1, 'test', 'test', 'testPart2', 1, 'test', 1, 'test', false),
       (DEFAULT, 2, 'test', 'test', 'testPart3', 1, 'test', 1, 'test', false),
       (DEFAULT, 2, 'test', 'test', 'testPart4', 1, 'test', 1, 'test', false),
       (DEFAULT, 2, 'test', 'test', 'testPart5', 1, 'test', 1, 'test', false);

INSERT INTO bikeservicetest.public.part_item(part_id, amount, item_id, part_type, type)
VALUES (1, 1, 1, 'Part', 'Document'),
       (1, 1, 1, 'Part', 'Consumable'),
       (1, 1, 1, 'Part', 'Fastener'),
       (1, 1, 1, 'Part', 'Tool'),
       (1, 1, 1, 'Part', 'Part');

INSERT INTO bikeservicetest.public.users(id, password, active, username)
VALUES (DEFAULT, '$2a$10$I/0KJRXs78nl5Z.Whzs9L.1uBwf9E/gJwqkZtgIZfrwarrOmJ.mBK', true, 'test'),
       (DEFAULT, '$2a$10$NJqlcPwFazO1CF4oWQcJuerCYhpYnSehN8HdgRWyq1mLx/5814cdy', true, 'pavel');

INSERT INTO bikeservicetest.public.user_role(user_id, status)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');
