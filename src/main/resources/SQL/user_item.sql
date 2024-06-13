create table user_item
(
    user_id bigint not null
        constraint fkdcjo77iqbb0cwvcgvu5vh1qxi
            references users,
    type    varchar(255),
    item_id bigint
);

alter table user_item
    owner to postgres;

INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 1);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 2);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Manufacturer', 2);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Manufacturer', 3);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Manufacturer', 4);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Part', 6);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Fastener', 1);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Tool', 1);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Tool', 2);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Consumable', 2);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Part', 7);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Bike', 2);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 6);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 9);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 10);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 11);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 12);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 17);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 18);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 19);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 21);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 26);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Fastener', 6);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Fastener', 7);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Fastener', 8);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 28);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Fastener', 10);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Part', 16);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Part', 17);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Part', 23);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 32);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Part', 25);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Tool', 3);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Document', 5);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (1, 'Fastener', 5);
INSERT INTO public.user_item (user_id, type, item_id) VALUES (50, 'Part', 26);
