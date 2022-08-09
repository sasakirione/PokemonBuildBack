create sequence gameversions_id_seq
    as integer;

alter sequence gameversions_id_seq owner to sasakirione;

create table abilities
(
    id   serial
        primary key,
    name varchar(20) not null
        constraint abilities_name_unique
            unique
);

alter table abilities
    owner to sasakirione;

create table game_versions
(
    id   integer default nextval('gameversions_id_seq'::regclass) not null
        constraint gameversions_pkey
            primary key,
    name varchar(30)                                              not null
        constraint gameversions_name_unique
            unique,
    gen  integer                                                  not null
);

alter table game_versions
    owner to sasakirione;

alter sequence gameversions_id_seq owned by game_versions.id;

create table goods
(
    id   serial
        primary key,
    name varchar(20) not null
        constraint goods_name_unique
            unique
);

alter table goods
    owner to sasakirione;

create table pokemons
(
    id             serial
        primary key,
    name           varchar(10)           not null,
    form_name      varchar(20),
    dex_no         integer               not null,
    form_no        integer default 1     not null,
    base_h         integer               not null,
    base_a         integer               not null,
    base_b         integer               not null,
    base_c         integer               not null,
    base_d         integer               not null,
    base_s         integer               not null,
    icon           varchar(100)          not null,
    icon2          varchar(100),
    is_restriction boolean default false not null
);

alter table pokemons
    owner to sasakirione;

create table natures
(
    id        serial
        primary key,
    name      varchar(10)           not null
        constraint natures_name_unique
            unique,
    is_majime boolean default false not null,
    up        integer               not null,
    down      integer               not null
);

alter table natures
    owner to sasakirione;

create table move_selects
(
    id   serial
        primary key,
    name varchar(10) not null
        constraint move_selects_name_unique
            unique
);

alter table move_selects
    owner to sasakirione;

create table types
(
    id   serial
        primary key,
    name varchar(10) not null
        constraint types_name_unique
            unique
);

alter table types
    owner to sasakirione;

create table moves
(
    id       serial
        primary key,
    name     varchar(30) not null
        constraint moves_name_unique
            unique,
    "select" integer     not null
        constraint fk_moves_select__id
            references move_selects
            on update restrict on delete restrict,
    type     integer     not null
        constraint fk_moves_type__id
            references types
            on update restrict on delete restrict,
    damage   integer
);

alter table moves
    owner to sasakirione;

create table users
(
    id      serial
        primary key,
    name    varchar(50)  default 'ポケモントレーナー'::character varying not null,
    auth_id varchar(255)                                        not null
        constraint users_auth_id_unique
            unique,
    profile text                                                not null,
    icon    varchar(100) default ''::character varying          not null
);

alter table users
    owner to sasakirione;

create table grown_pokemons
(
    id        serial
        primary key,
    pokemon   integer               not null
        constraint fk_grown_pokemons_pokemon__id
            references pokemons
            on update restrict on delete restrict,
    good      integer
        constraint fk_grown_pokemons_good__id
            references goods
            on update restrict on delete restrict,
    ability   integer               not null
        constraint fk_grown_pokemons_ability__id
            references abilities
            on update restrict on delete restrict,
    nature    integer               not null
        constraint fk_grown_pokemons_nature__id
            references natures
            on update restrict on delete restrict,
    move1     integer
        constraint fk_grown_pokemons_move1__id
            references moves
            on update restrict on delete restrict,
    move2     integer
        constraint fk_grown_pokemons_move2__id
            references moves
            on update restrict on delete restrict,
    move3     integer
        constraint fk_grown_pokemons_move3__id
            references moves
            on update restrict on delete restrict,
    move4     integer
        constraint fk_grown_pokemons_move4__id
            references moves
            on update restrict on delete restrict,
    "evH"     integer default 0     not null,
    "evA"     integer default 0     not null,
    "evB"     integer default 0     not null,
    "evC"     integer default 0     not null,
    "evD"     integer default 0     not null,
    "evS"     integer default 0     not null,
    "ivH"     integer default 31    not null,
    "ivA"     integer default 31    not null,
    "ivB"     integer default 31    not null,
    "ivC"     integer default 31    not null,
    "ivD"     integer default 31    not null,
    "ivS"     integer default 31    not null,
    "isShiny" boolean default false not null,
    comment   text,
    "user"    integer               not null
        constraint fk_grown_pokemons_user__id
            references users
            on update restrict on delete restrict
);

alter table grown_pokemons
    owner to sasakirione;

create table pokemon_ability_map
(
    id        serial
        primary key,
    pokemon   integer               not null
        constraint fk_pokemon_ability_map_pokemon__id
            references pokemons
            on update restrict on delete restrict,
    ability   integer               not null
        constraint fk_pokemon_ability_map_ability__id
            references abilities
            on update restrict on delete restrict,
    is_hidden boolean default false not null
);

alter table pokemon_ability_map
    owner to sasakirione;

create table pokemon_builds
(
    id      serial
        primary key,
    name    varchar(100) not null,
    comment text,
    "user"  integer      not null
        constraint fk_pokemon_builds_user__id
            references users
            on update restrict on delete restrict
);

alter table pokemon_builds
    owner to sasakirione;

create table pokemon_build_map
(
    id         serial
        primary key,
    build      integer              not null
        constraint fk_pokemon_build_map_build__id
            references pokemon_builds
            on update restrict on delete restrict,
    pokemon    integer              not null
        constraint fk_pokemon_build_map_pokemon__id
            references grown_pokemons
            on update restrict on delete restrict,
    "isActive" boolean default true not null,
    comment    text
);

alter table pokemon_build_map
    owner to sasakirione;

create table pokemon_move_map
(
    id      serial
        primary key,
    pokemon integer not null
        constraint fk_pokemon_move_map_pokemon__id
            references pokemons
            on update restrict on delete restrict,
    move    integer not null
        constraint fk_pokemon_move_map_move__id
            references moves
            on update restrict on delete restrict,
    version integer not null
        constraint fk_pokemon_move_map_version__id
            references game_versions
            on update restrict on delete restrict
);

alter table pokemon_move_map
    owner to sasakirione;

create table pokemon_tags
(
    id    serial
        primary key,
    name  varchar(15) not null,
    color varchar(50) not null
);

alter table pokemon_tags
    owner to sasakirione;

create table pokemon_tag_map
(
    id      serial
        primary key,
    pokemon integer not null
        constraint fk_pokemon_tag_map_pokemon__id
            references pokemons
            on update restrict on delete restrict,
    tag     integer not null
        constraint fk_pokemon_tag_map_tag__id
            references pokemon_tags
            on update restrict on delete restrict
);

alter table pokemon_tag_map
    owner to sasakirione;

create table pokemon_type_map
(
    id      serial
        primary key,
    pokemon integer not null
        constraint fk_pokemon_type_map_pokemon__id
            references pokemons
            on update restrict on delete restrict,
    type    integer not null
        constraint fk_pokemon_type_map_type__id
            references types
            on update restrict on delete restrict
);

alter table pokemon_type_map
    owner to sasakirione;

