INSERT INTO rent_a_car (id, rating, version, address, description, name) VALUES (1, 0, 0, '52337 Logan Point', 'Automated bifurcated matrices', 'Reichel-Braun');
INSERT INTO rent_a_car (id, rating, version, address, description, name) VALUES (2, 0, 0, '205 Shoshone Way', 'Seamless neutral database', 'Smitham, Zulauf and Lebsack');
INSERT INTO rent_a_car (id, rating, version, address, description, name) VALUES (3, 0, 0, '5 Gale Pass', 'Business-focused user-facing capability', 'Corwin, Dietrich and Gerlach');

INSERT INTO hotel (id, version, rating, address, description, name, catalogue_id, location_id) VALUES (1, 0, 0, '41 Mallard Road', 'Versatile mobile emulation', 'Herman, Flatley and Gleichner', null, null);
INSERT INTO hotel (id, version, rating, address, description, name, catalogue_id, location_id) VALUES (2, 0, 0, '8 Starling Road', 'Secured clear-thinking challenge', 'Medhurst-Kozey', null, null);
INSERT INTO hotel (id, version, rating, address, description, name, catalogue_id, location_id) VALUES (3, 0, 0, '60431 Pond Crossing', 'Assimilated global implementation', 'Marquardt-Gusikowski', null, null);

INSERT INTO destination (id, name, version) VALUES (1, 'Kéfalos', 0);
INSERT INTO destination (id, name, version) VALUES (2, 'Kaducondong', 0);
INSERT INTO destination (id, name, version) VALUES (3, 'Čajniče', 0);

INSERT INTO room (id, version, rating, number_of_beds, room_type_id, floor_id) VALUES (1, 0, 0, 1, null, null);
INSERT INTO room (id, version, rating, number_of_beds, room_type_id, floor_id) VALUES (2, 0, 0, 1, null, null);
INSERT INTO room (id, version, rating, number_of_beds, room_type_id, floor_id) VALUES (3, 0, 0, 4, null, null);

INSERT INTO room_type (id, version, description, name, price_per_night, catalogue_id) VALUES (1, 0, 'zero defect', 'Red', 103.94, null);
INSERT INTO room_type (id, version, description, name, price_per_night, catalogue_id) VALUES (2, 0, 'moderator', 'Crimson', 142.03, null);
INSERT INTO room_type (id, version, description, name, price_per_night, catalogue_id) VALUES (3, 0, '24/7', 'Purple', 93.45, null);

INSERT INTO catalogue (id, version) VALUES (1, 0);
INSERT INTO catalogue (id, version) VALUES (2, 0);
INSERT INTO catalogue (id, version) VALUES (3, 0);

INSERT INTO floor (id, version, number, hotel_id) VALUES (1, 0, 1, 1);
INSERT INTO floor (id, version, number, hotel_id) VALUES (2, 0, 2, 1);
INSERT INTO floor (id, version, number, hotel_id) VALUES (3, 0, 3, 1);

INSERT INTO airline (id, rating, version, address, description, name, location_id) VALUES (1, 0, 0, '9 Springs Plaza', 'Self-enabling uniform Graphic Interface', 'Dickinson-Weissnat', null);
INSERT INTO airline (id, rating, version, address, description, name, location_id) VALUES (2, 0, 0, '28 Emmet Lane', 'Up-sized zero administration encoding', 'Treutel-Upton', null);
INSERT INTO airline (id, rating, version, address, description, name, location_id) VALUES (3, 0, 0, '2901 Killdeer Place', 'Ameliorated incremental installation', 'Jast-Bashirian', null);

/* Kreiranje obicnih korisnika */
INSERT INTO registered_user (id, version, authority, email, first_name, last_name, password, username, state, bonus_points, city, phone_number) VALUES (2, 0, 'REGULAR_USER', 'dabelwhite1@squidoo.com', 'Dre', 'Abelwhite', '$2a$10$2RMGI5/i/0.azyI7TuKV4enFAjx03rosiCSEy762SIMjSiAXgeQme', 'user2', 'ACTIVE', 0, 'Yanaul', '585-206-7728');
INSERT INTO registered_user (id, version, authority, email, first_name, last_name, password, username, state, bonus_points, city, phone_number) VALUES (3, 0, 'REGULAR_USER', 'cmurley2@freewebs.com', 'Charlene', 'Murley', '$2a$10$oqDJmCc7f70vJo/GlUNUdu16OmqKO2r1pimXri.A0oMIjc/92YQtG', 'user3', 'ACTIVE', 0, 'Shujāābād', '964-873-5449');
INSERT INTO registered_user (id, version, authority, email, first_name, last_name, password, username, state, bonus_points, city, phone_number) VALUES (4, 0, 'REGULAR_USER', 'asantora3@pinterest.com', 'Ailsun', 'Santora', '$2a$10$IPbFrxANVG6N6ISErWdz1O4d81QVUPu8Be60VZiyZtrvdCnmPbjAK', 'user4', 'ACTIVE', 0, 'Stenungsund', '839-156-2257');
INSERT INTO registered_user (id, version, authority, email, first_name, last_name, password, username, state, bonus_points, city, phone_number) VALUES (5, 0, 'REGULAR_USER', 'jdryburgh4@about.me', 'Jerald', 'Dryburgh', '$2a$10$x0vED9/rVYV5DrHffnFdoe3OTOfHoqltGS13Ujs1BDrC2UAxhVYQm', 'user5', 'ACTIVE', 0, 'Hisings Kärra', '957-805-5781');
INSERT INTO registered_user (id, version, authority, email, first_name, last_name, password, username, state, bonus_points, city, phone_number) VALUES (6, 0, 'REGULAR_USER', 'csedcole5@fc2.com', 'Candra', 'Sedcole', '$2a$10$fkv7Zafx1t2EmeqLBMb6R.NnLCo8tsJNThl0krFIkVnBDhGqnk.3m', 'user6', 'ACTIVE', 0, 'Kamieniec', '698-990-0514');
INSERT INTO registered_user (id, version, authority, email, first_name, last_name, password, username, state, bonus_points, city, phone_number) VALUES (7, 0, 'REGULAR_USER', 'vreddle6@businessweek.com', 'Velvet', 'Reddle', '$2a$10$T1uMM8e0u0kcznrZSKhV5ewXsATa362e4Juu0s5Lp.mFza877nTte', 'user7', 'ACTIVE', 0, 'Shu', '874-812-3594');
INSERT INTO registered_user (id, version, authority, email, first_name, last_name, password, username, state, bonus_points, city, phone_number) VALUES (8, 0, 'REGULAR_USER', 'wohollegan7@eventbrite.com', 'Wendye', 'O''Hollegan', '$2a$10$40.rqUbSb3FJX9NnMi3HIejRgTIn69jzUY2MZCOQBQkvbEMOYRD0m', 'user8', 'ACTIVE', 0, 'Oleiros', '126-539-0854');
INSERT INTO registered_user (id, version, authority, email, first_name, last_name, password, username, state, bonus_points, city, phone_number) VALUES (9, 0, 'REGULAR_USER', 'batyeo8@paginegialle.it', 'Boothe', 'Atyeo', '$2a$10$1yMBGMTvXY3bMjC8rT0b2.aJJKNDVXHqbolPZPXYYxaGG2rYPjGqu', 'user9', 'ACTIVE', 0, 'Ninghai', '961-344-1169');
/* Kreiranje prijateljstava */
INSERT INTO friendship (id,friends_since) VALUES (1,'2019-02-07 23:47:15');
INSERT INTO friendship (id,friends_since) VALUES (2,'2019-02-07 23:47:21');
/* Kreiranje prijateljstava i povezivanje */
INSERT INTO friendships_between_users (friendship_id,user_id) VALUES (1,6);
INSERT INTO friendships_between_users (friendship_id,user_id) VALUES (1,4);
INSERT INTO friendships_between_users (friendship_id,user_id) VALUES (2,3);
INSERT INTO friendships_between_users (friendship_id,user_id) VALUES (2,5);
/* Kreiranje zahteva za prijateljstva */
INSERT INTO friend_request (id,request_time,receiver_id,sender_id) VALUES (1,'2019-02-07 23:46:03',5,2);
INSERT INTO friend_request (id,request_time,receiver_id,sender_id) VALUES (3,'2019-02-07 23:46:13',9,6);
