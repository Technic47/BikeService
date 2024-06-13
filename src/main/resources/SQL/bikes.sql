create table bikes
(
    id           bigserial
        primary key,
    creator      bigint,
    description  varchar(255),
    link         varchar(255),
    name         varchar(255),
    picture      bigint,
    manufacturer bigint,
    model        varchar(255),
    value        varchar(255),
    valuename    varchar(255),
    is_shared    boolean,
    created      timestamp(6),
    updated      timestamp(6)
);

alter table bikes
    owner to postgres;

INSERT INTO public.bikes (id, creator, description, link, name, picture, manufacturer, model, value, valuename, is_shared, created, updated) VALUES (2, 1, 'Ам подвес', 'нету', 'Habbit 4', 19, 2, 'Habbit 4', 'none', null, false, null, null);
