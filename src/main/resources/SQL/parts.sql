create table parts
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

alter table parts
    owner to postgres;

INSERT INTO public.parts (id, creator, description, link, name, picture, manufacturer, model, value, valuename, is_shared, created, updated) VALUES (6, 1, 'TestDescription', 'nono', 'Fork 35mm', 14, 3, '666', 'none', null, true, '2023-10-30 16:01:00.939000', '2023-10-30 16:01:00.939000');
INSERT INTO public.parts (id, creator, description, link, name, picture, manufacturer, model, value, valuename, is_shared, created, updated) VALUES (7, 1, 'для тормоза', 'нет', 'Колодки', 18, 4, '222333', 'нет', null, false, '2023-10-31 00:20:25.632000', '2023-10-31 00:20:25.632000');
INSERT INTO public.parts (id, creator, description, link, name, picture, manufacturer, model, value, valuename, is_shared, created, updated) VALUES (16, 1, 'Прям как ты', 'нет', 'Тормоз', 1, 1, '222', 'нет', null, false, '2023-10-31 00:20:41.965000', '2023-10-31 00:20:41.965000');
INSERT INTO public.parts (id, creator, description, link, name, picture, manufacturer, model, value, valuename, is_shared, created, updated) VALUES (26, 50, 'rre', 'wewr', 'ewrwer', 1, 3, 'wer', 'wer', null, false, '2024-06-13 20:56:44.977000', '2024-06-13 20:56:44.977000');
INSERT INTO public.parts (id, creator, description, link, name, picture, manufacturer, model, value, valuename, is_shared, created, updated) VALUES (17, 1, 'Прям как ты', 'нет', 'Тормоз', 1, 1, '222', 'нет', null, false, '2023-08-28 11:46:50.939000', '2023-08-28 11:46:50.939000');
INSERT INTO public.parts (id, creator, description, link, name, picture, manufacturer, model, value, valuename, is_shared, created, updated) VALUES (19, 1, 'Прям как ты', 'нет', 'Тормоз', 1, 1, null, 'нет', null, false, '2023-08-28 18:12:49.338000', '2023-08-28 18:12:49.338000');
INSERT INTO public.parts (id, creator, description, link, name, picture, manufacturer, model, value, valuename, is_shared, created, updated) VALUES (20, 1, 'Прям как ты', 'нет', 'Тормоз', 1, 1, null, 'нет', null, false, '2023-10-26 17:37:25.432000', null);
INSERT INTO public.parts (id, creator, description, link, name, picture, manufacturer, model, value, valuename, is_shared, created, updated) VALUES (23, 1, 'Прям как ты', 'нет', 'Тормоз', 1, 1, null, 'нет', null, false, '2023-10-26 17:40:00.149000', null);
INSERT INTO public.parts (id, creator, description, link, name, picture, manufacturer, model, value, valuename, is_shared, created, updated) VALUES (25, 1, '123', '123', 'testtesttest', 1, 1, '123', '123', null, false, '2023-10-29 20:11:08.343000', '2023-10-29 20:11:08.343000');
