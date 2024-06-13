create table consumables
(
    id           bigint generated always as identity
        primary key,
    manufacturer bigint       not null,
    model        varchar(100) not null,
    name         varchar(100) not null,
    description  varchar(255),
    picture      bigint,
    link         varchar(255),
    value        varchar(255),
    creator      bigint,
    valuename    varchar(255),
    is_shared    boolean,
    created      timestamp(6),
    updated      timestamp(6)
);

alter table consumables
    owner to postgres;

INSERT INTO public.consumables (manufacturer, model, name, description, picture, link, value, creator, valuename, is_shared, created, updated) VALUES (1, 'EXPERT MEDIUM 10W', 'Motul Fork Oil 10W', 'Среднее вилочное масло', 9, 'https://www.motul.com/ru/ru/products/fork-oil-expert-medium-10w', '1', 1, null, true, null, null);
INSERT INTO public.consumables (manufacturer, model, name, description, picture, link, value, creator, valuename, is_shared, created, updated) VALUES (1, 'na', 'Стяжки', 'no description', 1, 'нет', '1', null, null, false, null, '2023-10-06 11:24:12.872000');
