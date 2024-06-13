create table manufacturers
(
    name        varchar(100) not null,
    id          bigint generated always as identity
        primary key,
    description varchar(255),
    picture     bigint,
    link        varchar(255),
    value       varchar(255),
    creator     bigint,
    valuename   varchar(255),
    is_shared   boolean,
    created     timestamp(6),
    updated     timestamp(6)
);

alter table manufacturers
    owner to postgres;

INSERT INTO public.manufacturers (name, description, picture, link, value, creator, valuename, is_shared, created, updated) VALUES ('Default', 'Default manufacture for everything', 1, 'none', 'none', 1, null, false, null, null);
INSERT INTO public.manufacturers (name, description, picture, link, value, creator, valuename, is_shared, created, updated) VALUES ('MOTUL', 'Производитель масел', 10, 'https://www.motul.com/ru/ru', 'Франция', 1, null, true, null, null);
INSERT INTO public.manufacturers (name, description, picture, link, value, creator, valuename, is_shared, created, updated) VALUES ('Cannondale', 'Американский производитель велосипедов', 7, 'https://www.cannondale.com/', 'USA', 1, null, true, null, null);
INSERT INTO public.manufacturers (name, description, picture, link, value, creator, valuename, is_shared, created, updated) VALUES ('Rock Shox', 'Производитель элементов подвески, часть SRAM', 11, 'https://www.sram.com/en/rockshox', 'USA', null, null, false, null, '2023-08-28 11:47:36.125000');
