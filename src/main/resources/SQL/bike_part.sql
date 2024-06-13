create table bike_part
(
    bike_id   bigint not null
        constraint fk76ddx5sggotqcyaljy61fk2c4
            references bikes,
    amount    integer,
    item_id   bigint,
    part_type varchar(255),
    type      varchar(255)
);

alter table bike_part
    owner to postgres;

INSERT INTO public.bike_part (bike_id, amount, item_id, part_type, type) VALUES (2, 1, 7, 'Bike', 'Part');
INSERT INTO public.bike_part (bike_id, amount, item_id, part_type, type) VALUES (2, 1000, 2, 'Bike', 'Consumable');
INSERT INTO public.bike_part (bike_id, amount, item_id, part_type, type) VALUES (2, 1, 6, 'Bike', 'Part');
INSERT INTO public.bike_part (bike_id, amount, item_id, part_type, type) VALUES (2, 1, 5, 'Bike', 'Document');
INSERT INTO public.bike_part (bike_id, amount, item_id, part_type, type) VALUES (2, 345, 8, 'Bike', 'Fastener');
