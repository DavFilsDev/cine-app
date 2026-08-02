create extension if not exists "uuid-ossp";

do
$$
    begin
        if not exists(select from pg_type where typname = 'user_role') then
            create type user_role as enum ('CLIENT', 'EMPLOYEE', 'MANAGER');
        end if;
    end
$$;

create table if not exists "user"
(
    id         varchar primary key default uuid_generate_v4(),
    role       user_role   not null default 'CLIENT',
    first_name varchar(100),
    last_name  varchar(100),
    birthdate  date,
    email      varchar(255) not null unique,
    password   text         not null,
    phone      varchar(30),
    joined_at  timestamptz         default now()
);
