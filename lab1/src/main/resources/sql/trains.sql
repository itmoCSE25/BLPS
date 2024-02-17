create table users (
    id serial primary key,
    username text not null,
    email text,
    phone text,
    password text not null,
    created_date timestamp with time zone,
    updated_date timestamp with time zone
);

create table trains (
    id serial primary key,
    train_num int not null
);

create table vans (
    id serial primary key,
    train_id int references trains(id) not null,
    van_type int not null
);

create table tickets (
    id serial primary key,
    user_id int references users(id) not null,
    route_id int references routes(id) not null
);

create table seats (
    id serial primary key,
    van_id int references vans(id) not null,
    ticket_id int references tickets(id)
);

create table stations (
    id serial primary key,
    name text not null
);

create table routes (
    id serial primary key,
    departure_station_id int references stations(id) not null,
    arrival_station_id int references stations(id) not null,
    departure_time timestamp with time zone,
    arrival_time timestamp with time zone,
    train_id int references trains(id)
);
