INSERT INTO users (username, email, phone, password, created_date, updated_date)
VALUES ('Alex123', 'alex123@example.com', '123-456-7890', 'pass1234', NOW(), NOW()),
       ('Betty456', 'betty456@example.com', '456-789-1230', 'bettysecret', NOW(), NOW());

INSERT INTO stations (name)
VALUES ('Central Station'),
       ('East Station'),
       ('West Station'),
       ('South Station');

INSERT INTO routes (departure_station_id, arrival_station_id, departure_time, arrival_time)
VALUES (1, 2, '2023-05-01 08:00:00+00', '2023-05-01 10:00:00+00'),
       (3, 4, '2023-05-01 08:00:00+00', '2023-05-01 10:00:00+00');

INSERT INTO trains (train_num, route_id)
VALUES (101, 5),
       (110, 6);


INSERT INTO vans (train_id, van_type, van_num)
VALUES (3, 2, 1),
       (4, 1, 12);


INSERT INTO seats (van_id)
VALUES (2),
       (3),
       (3);

create table users
(
    id       serial primary key,
    username text not null,
    password text not null,
    role     text not null
);

alter table stations add unique (name);
alter table routes add unique (departure_station_id, arrival_station_id);
alter table vans add unique (train_id, van_num);
alter table trains add unique (train_num, route_id);




