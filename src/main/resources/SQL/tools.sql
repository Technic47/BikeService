create table tools
(
    id           bigint generated always as identity
        primary key,
    manufacturer bigint,
    model        varchar(100) not null,
    name         varchar(100) not null,
    description  varchar(255) not null,
    picture      bigint,
    value        varchar(255),
    link         varchar(255),
    creator      bigint,
    valuename    varchar(255),
    is_shared    boolean,
    created      timestamp(6),
    updated      timestamp(6)
);

alter table tools
    owner to postgres;

INSERT INTO public.tools (manufacturer, model, name, description, picture, value, link, creator, valuename, is_shared, created, updated) VALUES (1, 'стандартная', 'Труборез', 'Роликовый', 16, '35мм', 'нет', 1, null, true, null, null);
INSERT INTO public.tools (manufacturer, model, name, description, picture, value, link, creator, valuename, is_shared, created, updated) VALUES (3, 'Test123', 'TestThing', 'no', 1, '666', '', 2, null, false, null, null);
INSERT INTO public.tools (manufacturer, model, name, description, picture, value, link, creator, valuename, is_shared, created, updated) VALUES (1, '', 'Измеритель цепи', 'Измерение растяжения', 29, 'стандарт', 'нет', 1, null, false, null, null);
