create table if not exists "projection"
(
    id         varchar primary key default uuid_generate_v4(),
    datetime   timestamptz    not null,
    seat_price numeric(10, 2) not null,
    movie_id   varchar        not null references "movie" (id),
    room_id    varchar        not null references "room" (id)
);
