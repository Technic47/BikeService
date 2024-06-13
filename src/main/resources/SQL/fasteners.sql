create table fasteners
(
    name        varchar(100) not null,
    description varchar(255),
    id          bigint generated always as identity
        primary key,
    picture     bigint,
    link        varchar(255),
    value       varchar(255),
    creator     bigint,
    valuename   varchar(255),
    is_shared   boolean,
    created     timestamp(6),
    updated     timestamp(6)
);

alter table fasteners
    owner to postgres;

INSERT INTO public.fasteners (name, description, picture, link, value, creator, valuename, is_shared, created, updated) VALUES ('Болт тормоза', 'Для калипера', 1, 'нет', 'М6х25', 5, null, true, null, null);
INSERT INTO public.fasteners (name, description, picture, link, value, creator, valuename, is_shared, created, updated) VALUES ('TestBolt', 'asdfasdf', 1, 'asdfsadf', 'sadfsadf', 5, null, true, null, null);
INSERT INTO public.fasteners (name, description, picture, link, value, creator, valuename, is_shared, created, updated) VALUES ('newtests', 'sdfgdsfg', 1, 'dsfgdsfg', 'dfg ', 2, null, false, null, null);
INSERT INTO public.fasteners (name, description, picture, link, value, creator, valuename, is_shared, created, updated) VALUES ('12345', 'sdgfweg', 1, 'sdfa', 'fegfsd', 1, null, false, null, null);
INSERT INTO public.fasteners (name, description, picture, link, value, creator, valuename, is_shared, created, updated) VALUES ('M6 bolt', 'Крепление тормоза', 1, 'no', '8.8', 1, null, false, null, null);
INSERT INTO public.fasteners (name, description, picture, link, value, creator, valuename, is_shared, created, updated) VALUES ('testBolt', 'dddd', 1, '', 'm8', 1, null, false, null, null);
