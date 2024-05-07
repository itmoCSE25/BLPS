create table users
(
    id           serial primary key,
    username     text not null,
    email        text,
    phone        text,
    password     text not null,
    created_date timestamp with time zone,
    updated_date timestamp with time zone
);

create table stations
(
    id   serial primary key,
    name text not null
);

create table routes
(
    id                   serial primary key,
    departure_station_id int references stations (id) on delete cascade not null,
    arrival_station_id   int references stations (id) on delete cascade not null,
    departure_time       timestamp with time zone,
    arrival_time         timestamp with time zone
);

create table trains
(
    id        serial primary key,
    train_num int not null,
    route_id  int references routes (id)
);

create table vans
(
    id       serial primary key,
    train_id int references trains (id) not null,
    van_type int                        not null,
    van_num  int                        not null,
    unique (van_num, train_id)
);

create table seats
(
    id     serial primary key,
    van_id int references vans (id) on delete cascade not null
);

create table tickets
(
    id                 serial primary key,
    user_id            int references users (id) on delete cascade  not null,
    name               text                                         not null,
    surname            text                                         not null,
    route_id           int references routes (id) on delete cascade not null,
    train_id           int references trains (id) on delete cascade not null,
    van_id             int references vans (id) on delete cascade   not null,
    seat_id            int references seats (id) on delete cascade  not null,
    unique (route_id, train_id, van_id, seat_id)
);

create table receipt
(
    id serial primary key,
    user_id int references users(id) on delete cascade not null,
    ticket_id int references tickets(id) on delete cascade not null,
    transaction_id int,
    transaction_status int,
    unique (transaction_id)
);

create table test
(
    id serial primary key,
    name text
)
