TRUNCATE
    bikeservicetest.public.pictures RESTART IDENTITY CASCADE;

INSERT INTO bikeservicetest.public.pictures(id, name)
VALUES (DEFAULT, 'test'),
       (DEFAULT, 'test2'),
       (DEFAULT, 'test3');