create table public.violations
(
    id                bigserial
        constraint violations_pk
            primary key,
    equipments_serial varchar(100)                        not null
        constraint violations_equipments_serial_fk
            references public.equipments
            on update cascade on delete restrict,
    occurrence_date   timestamp with time zone            not null,
    measured_speed    double precision                    not null,
    considered_speed  double precision                    not null,
    regulated_speed   double precision                    not null,
    picture           varchar(300)                        not null,
    type              varchar(70)                         not null,
    created_at        timestamp default CURRENT_TIMESTAMP not null,
    updated_at        timestamp default CURRENT_TIMESTAMP not null
);

alter table public.violations
    owner to admin;

create index violations_equipments_serial_occurrence_date_index
    on public.violations (equipments_serial asc, occurrence_date desc);



create table public.equipments
(
    serial     varchar(100)                        not null
        constraint equipments_pk
            primary key,
    model      varchar(50)                         not null,
    address    varchar(200)                        not null,
    latitude   numeric(10, 8)                      not null,
    longitude  numeric(11, 8)                      not null,
    active     boolean                             not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null
);

alter table public.equipments
    owner to admin;

