create table part_item
(
    part_id   bigint not null
        constraint fkcie20obae27hvcchl0wxq5w5l
            references parts,
    amount    integer,
    item_id   bigint,
    part_type varchar(255),
    type      varchar(255)
);

alter table part_item
    owner to postgres;

INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (19, 1, 5, 'Part', 'Document');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (19, 1, 11, 'Part', 'Document');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (19, 1, 1, 'Part', 'Tool');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (19, 1, 7, 'Part', 'Part');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (20, 1, 1, 'Part', 'Tool');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (20, 1, 3, 'Part', 'Tool');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (25, 1, 1, 'Part', 'Tool');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (25, 1, 5, 'Part', 'Document');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (25, 1, 1, 'Part', 'Consumable');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (6, 1, 2, 'Part', 'Consumable');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (6, 1, 5, 'Part', 'Document');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (6, 1, 2, 'Part', 'Fastener');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (6, 1, 16, 'Part', 'Part');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (7, 50, 2, 'Part', 'Consumable');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (7, 1, 6, 'Part', 'Part');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (7, 6, 1, 'Part', 'Fastener');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (16, 1222, 2, 'Part', 'Consumable');
INSERT INTO public.part_item (part_id, amount, item_id, part_type, type) VALUES (16, 1, 5, 'Part', 'Document');
