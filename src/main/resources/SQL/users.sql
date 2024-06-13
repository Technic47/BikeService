create table users
(
    id          bigserial
        constraint users_pk
            primary key,
    active      boolean,
    password    varchar(1000),
    status      smallint[],
    username    varchar(255)
        constraint uk_r43af9ap4edm43mmtq01oddj6
            unique,
    provider    varchar(255),
    email       varchar(255)
        constraint uk_6dotkott2kjsp8vw4d0m25fb7
            unique,
    enabled     boolean,
    created     timestamp(6),
    updated     timestamp(6),
    last_log_in timestamp(6)
);

alter table users
    owner to postgres;

create unique index users_id_uindex
    on users (id);

INSERT INTO public.users (id, active, password, status, username, provider, email, enabled, created, updated, last_log_in) VALUES (40, false, '$2a$10$c2EqYlI3cNtsxopXAZiq2eEt7kQOrQkkU/GfVMhVRXRFYGiC3iIJ2', null, 'technic47', 'LOCAL', 'technic47@gmail.com', true, '2023-08-29 10:38:47.613000', null, null);
INSERT INTO public.users (id, active, password, status, username, provider, email, enabled, created, updated, last_log_in) VALUES (6, false, '$2a$10$QeT8PIEjxGMqPQv2kugCaumbMVvy3yGZ.yys.DO3KkvgHW5/2ZZ6a', null, 'Andrey123', 'LOCAL', null, true, null, null, null);
INSERT INTO public.users (id, active, password, status, username, provider, email, enabled, created, updated, last_log_in) VALUES (1, true, '$2a$10$61Tp4teO3dlfzgfE9XWbCe4oNiwoXX50ntFoXGKU6bCrZ/UOsAuBm', null, 'pavel', 'LOCAL', 'pavel', true, null, null, '2024-06-13 20:04:58.247000');
INSERT INTO public.users (id, active, password, status, username, provider, email, enabled, created, updated, last_log_in) VALUES (50, true, '$2a$10$9K5dvZyA5Y1.sXX0vwvlKOVtH4Q0//YJgMfcUaqzPS6X//aPcOGSC', null, 'admin', 'LOCAL', 'admin', true, '2024-06-13 20:55:58.314000', null, '2024-06-13 20:56:12.515000');
