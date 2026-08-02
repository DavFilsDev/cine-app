create table if not exists "seat"
(
    id      varchar primary key default uuid_generate_v4(),
    number  varchar(20) not null,
    room_id varchar     not null references "room" (id)
);
