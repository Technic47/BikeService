create table user_role
(
    user_id bigint not null
        constraint fkj345gk1bovqvfame88rcx7yyx
            references users,
    status  varchar(255)
);

alter table user_role
    owner to postgres;

INSERT INTO public.user_role (user_id, status) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.user_role (user_id, status) VALUES (6, 'ROLE_USER');
INSERT INTO public.user_role (user_id, status) VALUES (40, 'ROLE_USER');
INSERT INTO public.user_role (user_id, status) VALUES (50, 'ROLE_ADMIN');
