create table if not exists "reservation"
(
    id            varchar primary key default uuid_generate_v4(),
    created_at    timestamptz not null default now(),
    projection_id varchar     not null references "projection" (id),
    seat_id       varchar     not null references "seat" (id),
    user_id       varchar     not null references "user" (id),
    unique (projection_id, seat_id)
    );