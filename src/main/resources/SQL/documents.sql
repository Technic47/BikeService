create table documents
(
    id          bigint generated always as identity
        primary key,
    name        varchar(100) not null,
    description varchar(255),
    link        text,
    picture     bigint,
    value       varchar(255),
    creator     bigint,
    valuename   varchar(255),
    is_shared   boolean,
    created     timestamp(6),
    updated     timestamp(6)
);

alter table documents
    owner to postgres;

INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('NewTestDoc', 'asdf', 'ertert', 1, 'ertert', 2, null, false, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('sdfghsdfg', 'sdfgs', 'ffff', 1, 'ffff', 1, null, false, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('dfgsdf', 'gsdfg', 'gdfg', 1, 'gdfg', 1, null, false, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('sgdgsdfg', 'sdfg', ' w er t', 1, ' w er t', 1, null, false, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('vrtvvrv', 'fbfdtbvy', 'tvrtrv', 1, 'tvrtrv', 1, null, false, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('asdfasdf', 'asdfa', 'dfasdf', 1, 'dfasdf', 1, null, true, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('xvbcvb', 'xcvbxcvbxc', 'bxcvbxcvb', 1, 'bxcvbxcvb', 1, null, false, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('xcvbxc', 'bsdfdv f', 'gbsdfsdf', 1, 'gbsdfsdf', 1, null, false, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('sfrve', 'qawef', 'fwref', 1, 'fwref', 1, null, false, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('sdfgs', 'dfgsdgf', 'sdfgsdfg', 1, 'sdfgsdfg', 1, null, false, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('sdfgvsdfv', 'sdvfgsdvgs', 'sdvfv', 1, 'sdvfv', 1, null, false, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('sadfasdf', 'qe', 'dddd', 1, 'dddd', 1, null, false, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('xzfzx', 'zxvbxc', 'vbcxvb', 1, 'vbcxvb', 33, null, false, null, null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('sdfvgsdf', 'vsdfgv sc', 'sdsss', 1, 'sdsss', 1, null, false, '2023-10-29 16:02:00.269000', '2023-10-29 16:02:00.269000');
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('testtest', 'rrrr', '33333', 1, '33333', 1, null, true, '2023-10-29 19:38:38.668000', null);
INSERT INTO public.documents (name, description, link, picture, value, creator, valuename, is_shared, created, updated) VALUES ('123455555', 'No description', 'no-link', 1, 'no-link', 1, null, false, '2023-10-31 00:20:51.734000', '2023-10-31 00:20:51.734000');
