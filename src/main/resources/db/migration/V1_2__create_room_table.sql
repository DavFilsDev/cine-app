create table if not exists "room"
(
    id       varchar primary key default uuid_generate_v4(),
    number   varchar(20) not null,
    capacity integer     not null
);
