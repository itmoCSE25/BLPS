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
       (5, 6, '2023-05-01 08:00:00+00', '2023-05-01 10:00:00+00' );

INSERT INTO trains (train_num, route_id)
VALUES (101, 1),
       (110, 2);


INSERT INTO vans (train_id, van_type, van_num)
VALUES (1, 2, 1);


INSERT INTO seats (van_id)
VALUES (1);

-- INSERT INTO tickets (user_id, name, surname, route_id, train_id, van_id, seat_id, transaction_status, transaction_id)
-- VALUES (1, 'Alex', 'Johnson', 1, 1, 1, 1, 0, 111);






