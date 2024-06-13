create table pictures
(
    id   bigint generated always as identity
        primary key,
    name text
);

alter table pictures
    owner to postgres;

INSERT INTO public.pictures (name) VALUES ('noImage.jpg');
INSERT INTO public.pictures (name) VALUES ('Cannondale.png');
INSERT INTO public.pictures (name) VALUES ('Motul 10W.jpg');
INSERT INTO public.pictures (name) VALUES ('Logo-Motul.png');
INSERT INTO public.pictures (name) VALUES ('RockShox.jpg');
INSERT INTO public.pictures (name) VALUES ('Shock spring.jpg');
INSERT INTO public.pictures (name) VALUES ('SHIMANO.png');
INSERT INTO public.pictures (name) VALUES ('c140mmblack3qdv2.jpg');
INSERT INTO public.pictures (name) VALUES ('360_6a#626187#image_1024.jpg');
INSERT INTO public.pictures (name) VALUES ('pads_shimano_j05a_rf_01.jpg');
INSERT INTO public.pictures (name) VALUES ('c21_c23501m_habit_4_slt_3q_2048377.jpg');
INSERT INTO public.pictures (name) VALUES ('-000336920_p_s.jpg');
INSERT INTO public.pictures (name) VALUES ('pngwing.com (1).png');
