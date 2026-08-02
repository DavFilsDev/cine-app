create table if not exists "movie"
(
    id          varchar primary key default uuid_generate_v4(),
    title       varchar(255) not null,
    genre       varchar(255)         default '',
    description text,
    duration    bigint
);
