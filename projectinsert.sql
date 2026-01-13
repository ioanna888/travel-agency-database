USE travel_agency ; 
-- branch 

INSERT INTO branch (br_code, br_street, br_num, br_city, br_manager_AT) VALUES 
(1, 'Ermou', 15, 'Athens', NULL),
(2, 'Tsimiski', 45, 'Thessaloniki', NULL),
(3, 'Maizonos', 12, 'Patras', NULL),
(4, '25th August', 10, 'Heraklion', NULL),
(5, 'Dodonis', 5, 'Ioannina', NULL),
(6, 'Sachtouri', 22, 'Pyrgos', NULL);

-- eisagogi workers
-- admins
INSERT INTO worker (wrk_AT, wrk_name, wrk_lname, wrk_email, wrk_salary, wrk_br_code) VALUES 
('AT1001', 'Nikos', 'Papadopoulos', 'nikos@travel.gr', 1500.00, 1),
('AT1002', 'Maria', 'Georgiou', 'maria@travel.gr', 1450.00, 2),
('AT1003', 'Eleni', 'Dimitriou', 'eleni@travel.gr', 1400.00, 3),
('AT1004', 'Kostas', 'Nikolaou', 'kostas@travel.gr', 1550.00, 4),
('AT1005', 'Anna', 'Vasiliou', 'anna@travel.gr', 1600.00, 5),
('AT1006', 'Giorgos', 'Petrou', 'giorgos.p@travel.gr', 1350.00, 6),
('AT1007', 'Sofia', 'Andreou', 'sofia.a@travel.gr', 1250.00, 1),
('AT1008', 'Dimitris', 'Ioannou', 'dimitris.i@travel.gr', 1300.00, 2),
('AT1009', 'Katerina', 'Xanthou', 'katerina.x@travel.gr', 1280.00, 1),
('AT1010', 'Panagiotis', 'Lymperis', 'panos.l@travel.gr', 1320.00, 2);

INSERT INTO admin (adm_AT, adm_type, adm_diploma) VALUES 
('AT1001', 'ADMINISTRATIVE', 'MBA Management'),
('AT1002', 'ACCOUNTING', 'BSc Economics'),
('AT1003', 'LOGISTICS', 'MSc Logistics'),
('AT1004', 'ADMINISTRATIVE', 'Tourism Management'),
('AT1005', 'ACCOUNTING', 'PhD Finance'),
('AT1006', 'LOGISTICS', 'Supply Chain Cert'),
('AT1007', 'ADMINISTRATIVE', 'HR Diploma'),
('AT1008', 'ACCOUNTING', 'BSc Math'),
('AT1009', 'LOGISTICS', 'Transport MSc'),
('AT1010', 'ADMINISTRATIVE', 'Public Relations');

-- guides
INSERT INTO worker (wrk_AT, wrk_name, wrk_lname, wrk_email, wrk_salary, wrk_br_code) VALUES
('AT2001', 'Ioanna', 'Makri', 'ioanna@travel.gr', 1000.00, 1),
('AT2002', 'Vasilis', 'Raptis', 'vasilis@travel.gr', 1050.00, 2),
('AT2003', 'Fotis', 'Kouros', 'fotis@travel.gr', 1020.00, 3),
('AT2004', 'Marina', 'Leri', 'marina@travel.gr', 1080.00, 4),
('AT2005', 'Niki', 'Panagaki', 'niki@travel.gr', 950.00, 5),
('AT2006', 'Tasos', 'Vougas', 'tasos@travel.gr', 980.00, 6),
('AT2007', 'Elena', 'Kosta', 'elena@travel.gr', 1100.00, 1),
('AT2008', 'Spyros', 'Manias', 'spyros@travel.gr', 1030.00, 2);

INSERT INTO guide (gui_AT, gui_cv) VALUES 
('AT2001', 'Expert in Classical Antiquity'),
('AT2002', 'Specializes in Food Tours'),
('AT2003', 'Hiking and Nature specialist'),
('AT2004', 'Knossos Palace certified guide'),
('AT2005', 'Epirus region expert'),
('AT2006', 'Religious tourism specialist'),
('AT2007', 'Art History Major'),
('AT2008', 'Adventure tourism guide');


-- drivers
INSERT INTO worker (wrk_AT, wrk_name, wrk_lname, wrk_email, wrk_salary, wrk_br_code) VALUES 
('AT3001', 'Yiannis', 'Sokratous', 'yiannis.s@travel.gr', 900.00, 1),
('AT3002', 'Christos', 'Papas', 'christos.p@travel.gr', 920.00, 2),
('AT3003', 'Thanasis', 'Oikonomou', 'thanasis.o@travel.gr', 910.00, 3),
('AT3004', 'Petros', 'Alexiou', 'petros.a@travel.gr', 930.00, 4),
('AT3005', 'Michalis', 'Zervas', 'michalis@travel.gr', 880.00, 5),
('AT3006', 'Stelios', 'Rokas', 'stelios@travel.gr', 890.00, 6),
('AT3007', 'Andreas', 'Fotiou', 'andreas@travel.gr', 950.00, 1),
('AT3008', 'Takis', 'Vlachos', 'takis@travel.gr', 940.00, 2);

INSERT INTO driver (drv_AT, drv_license, drv_route, drv_experience) VALUES 
('AT3001', 'D', 'LOCAL', 5),
('AT3002', 'C', 'ABROAD', 12),
('AT3003', 'D', 'LOCAL', 3),
('AT3004', 'B', 'ABROAD', 8),
('AT3005', 'C', 'LOCAL', 15),
('AT3006', 'D', 'LOCAL', 4),
('AT3007', 'D', 'ABROAD', 10),
('AT3008', 'B', 'ABROAD', 6);

-- languages
INSERT INTO language_ref (lang_code, lang_name) VALUES 
('EL', 'Greek'), ('EN', 'English'), ('FR', 'French'), 
('DE', 'German'), ('IT', 'Italian'), ('ES', 'Spanish'),
('RU', 'Russian'), ('ZH', 'Chinese');


-- managers kai branch exoun kykliki sxesi- errors stin dimiourgia tou enos xoris to allo gia auto valame null
UPDATE branch SET br_manager_AT = 'AT1001' WHERE br_code = 1;
UPDATE branch SET br_manager_AT = 'AT1002' WHERE br_code = 2;
UPDATE branch SET br_manager_AT = 'AT1003' WHERE br_code = 3;
UPDATE branch SET br_manager_AT = 'AT1004' WHERE br_code = 4;
UPDATE branch SET br_manager_AT = 'AT1005' WHERE br_code = 5;
UPDATE branch SET br_manager_AT = 'AT1006' WHERE br_code = 6;

-- manages / poios manager poio branch
INSERT INTO manages (mng_adm_AT, mng_br_code) VALUES 
('AT1001', 1), ('AT1002', 2), ('AT1003', 3),
('AT1004', 4), ('AT1005', 5), ('AT1006', 6);

-- languages ton guides
INSERT INTO languages (lng_gui_AT, lng_language_code) VALUES 
('AT2001', 'EN'), ('AT2001', 'FR'), 
('AT2002', 'DE'), ('AT2002', 'EN'),
('AT2003', 'IT'), ('AT2004', 'EN'), 
('AT2004', 'RU'), ('AT2005', 'EN'),
('AT2006', 'ES'), ('AT2007', 'FR'),
('AT2007', 'IT'), ('AT2008', 'DE');

-- phones
INSERT INTO phones (ph_br_code, ph_number) VALUES 
(1, '2101010101'), (1, '2101010102'),
(2, '2310202020'), (2, '2310202021'),
(3, '2610303030'), (3, '2610303031'),
(4, '2810404040'), (5, '2651050505'),
(6, '2421060606'), (1, '2101010103');

-- destinations
INSERT INTO destination (dst_id, dst_name, dst_descr, dst_rtype, dst_language_code, dst_location) VALUES 
(1, 'Paris', 'The city of love', 'ABROAD', 'FR', 101),
(2, 'London', 'Big Bang Theory', 'ABROAD', 'EN', 102),
(3, 'Berlin', 'Modern History', 'ABROAD', 'DE', 103),
(4, 'Rome', 'Gladiator', 'ABROAD', 'IT', 104),
(5, 'Madrid', 'Spanish Culture', 'ABROAD', 'ES', 105),
(6, 'Chania', 'Perfect Getaway', 'LOCAL', 'EL', 201),
(7, 'Meteora', 'Monasteries', 'LOCAL', 'EL', 202),
(8, 'Delphi', 'Mantissa', 'LOCAL', 'EL', 203),
(9, 'Olympia', 'Olympic Games', 'LOCAL', 'EL', 204),
(10, 'Santorini', 'Volcano View', 'LOCAL', 'EL', 205),
(11, 'Zagori', 'Stone Bridges', 'LOCAL', 'EL', 206);

-- trip
INSERT INTO trip (tr_id, tr_departure, tr_return, tr_maxseats, tr_cost_adult, tr_cost_child, tr_status, tr_min_participants, tr_br_code, tr_gui_AT, tr_drv_AT) VALUES 
(1, '2025-06-01 08:00:00', '2025-06-05 20:00:00', 40, 500.00, 250.00, 'PLANNED', 15, 1, 'AT2001', 'AT3007'),
(2, '2025-07-10 09:00:00', '2025-07-15 18:00:00', 30, 600.00, 300.00, 'CONFIRMED', 10, 1, 'AT2007', 'AT3008'),
(3, '2025-08-01 07:00:00', '2025-08-07 21:00:00', 50, 450.00, 200.00, 'PLANNED', 20, 2, 'AT2002', 'AT3002'),
(4, '2025-09-15 08:30:00', '2025-09-20 19:30:00', 45, 550.00, 275.00, 'COMPLETED', 15, 2, 'AT2008', 'AT3008'),
(5, '2025-10-05 10:00:00', '2025-10-10 22:00:00', 25, 300.00, 150.00, 'CANCELLED', 10, 3, 'AT2003', 'AT3003'),
(6, '2025-12-20 06:00:00', '2025-12-27 23:00:00', 50, 700.00, 350.00, 'PLANNED', 20, 3, 'AT2003', 'AT3006'),
(7, '2026-01-05 09:00:00', '2026-01-10 17:00:00', 35, 400.00, 200.00, 'CONFIRMED', 12, 4, 'AT2004', 'AT3004'),
(8, '2025-05-20 08:00:00', '2025-05-25 20:00:00', 40, 800.00, 400.00, 'ACTIVE', 15, 4, 'AT2004', 'AT3002'),
(9, '2025-06-15 09:00:00', '2025-06-20 18:00:00', 30, 350.00, 175.00, 'PLANNED', 10, 5, 'AT2005', 'AT3005'),
(10, '2025-07-20 07:00:00', '2025-07-25 21:00:00', 50, 380.00, 190.00, 'PLANNED', 20, 5, 'AT2005', 'AT3001'),
(11, '2025-08-10 08:30:00', '2025-08-15 19:30:00', 45, 420.00, 210.00, 'CONFIRMED', 15, 6, 'AT2006', 'AT3006'),
(12, '2025-09-01 10:00:00', '2025-09-05 22:00:00', 25, 500.00, 250.00, 'PLANNED', 10, 6, 'AT2006', 'AT3003'),
(13, '2025-11-15 06:00:00', '2025-11-20 23:00:00', 50, 600.00, 300.00, 'PLANNED', 20, 1, 'AT2007', 'AT3007'),
(14, '2026-02-10 09:00:00', '2026-02-15 17:00:00', 35, 450.00, 225.00, 'CONFIRMED', 12, 2, 'AT2008', 'AT3004'),
(15, '2025-06-20 08:00:00', '2025-06-27 20:00:00', 40, 950.00, 450.00, 'PLANNED', 15, 1, 'AT2001', 'AT3008');

-- travel to

INSERT INTO travel_to (to_tr_id, to_dst_id, to_arrival, to_departure) VALUES 
(1, 1, '2025-06-01', '2025-06-04'), 
(2, 2, '2025-07-10', '2025-07-14'),
(3, 3, '2025-08-01', '2025-08-06'), 
(4, 1, '2025-09-15', '2025-09-19'),
(5, 6, '2025-10-05', '2025-10-09'), 
(6, 2, '2025-12-20', '2025-12-26'),
(7, 7, '2026-01-05', '2026-01-09'),
(8, 4, '2025-05-20', '2025-05-24'),
(9, 11, '2025-06-15', '2025-06-19'),
(10, 8, '2025-07-20', '2025-07-24'),
(11, 9, '2025-08-10', '2025-08-14'),
(12, 10, '2025-09-01', '2025-09-04'),
(13, 5, '2025-11-15', '2025-11-19'),
(14, 3, '2026-02-10', '2026-02-14'),
(15, 4, '2025-06-21', '2025-06-26');

-- event
INSERT INTO event (ev_tr_id, ev_start, ev_end, ev_descr) VALUES 
(1, '2025-06-02 10:00:00', '2025-06-02 14:00:00', 'Louvre Museaum'),
(1, '2025-06-03 19:00:00', '2025-06-03 22:00:00', 'Eiffel Dinner'),
(2, '2025-07-11 09:00:00', '2025-07-11 13:00:00', 'British Museum'),
(2, '2025-07-12 15:00:00', '2025-07-12 18:00:00', 'London Eye'),
(3, '2025-08-02 10:00:00', '2025-08-02 16:00:00', 'Berlin Wall'),
(3, '2025-08-04 20:00:00', '2025-08-04 23:00:00', 'Nightlife Tour'),
(4, '2025-09-16 11:00:00', '2025-09-16 15:00:00', 'Seine Cruise'),
(5, '2025-10-06 10:00:00', '2025-10-06 14:00:00', 'Chania Old Town'),
(6, '2025-12-21 18:00:00', '2025-12-21 21:00:00', 'Xmas Market'),
(7, '2026-01-06 09:00:00', '2026-01-06 12:00:00', 'Meteora Hike'),
(8, '2025-05-21 10:00:00', '2025-05-21 13:00:00', 'Colosseum Tour'),
(8, '2025-05-22 18:00:00', '2025-05-22 20:00:00', 'Pizza Tasting'),
(9, '2025-06-16 09:00:00', '2025-06-16 14:00:00', 'Vikos Gorge'),
(10, '2025-07-21 10:00:00', '2025-07-21 12:00:00', 'Oracle Site'),
(11, '2025-08-11 11:00:00', '2025-08-11 14:00:00', 'Stadium Visit'),
(12, '2025-09-02 17:00:00', '2025-09-02 20:00:00', 'Sunset Oia'),
(13, '2025-11-16 10:00:00', '2025-11-16 15:00:00', 'Prado Museum'),
(14, '2026-02-11 09:00:00', '2026-02-11 13:00:00', 'Reichstag Visit'),
(15, '2025-06-22 09:00:00', '2025-06-22 12:00:00', 'Vatican'),
(15, '2025-06-23 20:00:00', '2025-06-23 23:00:00', 'Trevi Fountain');

-- customers
INSERT INTO customer (cust_id, cust_name, cust_lname, cust_email, cust_phone, cust_address, cust_birth_date) VALUES 
(1, 'Giannis', 'Ioannou', 'gioann@gmail.com', '6901', 'Adr1', '1980-01-01'),
(2, 'Petros', 'Petrou', 'ppetrou@gmail.com', '6902', 'Adr2', '1985-02-02'),
(3, 'Maria', 'Dimitriou', 'mdim@gmail.com', '6903', 'Adr3', '1990-03-03'),
(4, 'Eleni', 'Ioannou', 'eioannou@gmail.com', '6904', 'Adr4', '1992-04-04'),
(5, 'Vasilis', 'Vasileiou', 'vvleiou@gmail.com', '6905', 'Adr5', '1975-05-05'),
(6, 'Dimitra', 'Nik', 'dnk@gmail.com', '6906', 'Adr6', '1988-06-06'),
(7, 'Kostas', 'Georgakopoulos', 'kg1234@gmail.com', '6907', 'Adr7', '1995-07-07'),
(8, 'Sofia', 'Pomer', 'sofiap@gmail.com', '6908', 'Adr8', '1999-08-08'),
(9, 'Nikos', 'Christou', 'nchristou@gmail.com', '6909', 'Adr9', '1982-09-09'),
(10, 'Katerina', 'Panouli', 'kapanouli@gmail.com', '6910', 'Adr10', '1991-10-10'),
(11, 'Alex', 'Alexiou', 'alex2@gmail.com', '6911', 'Adr11', '1983-01-11'),
(12, 'Zoe', 'Zerva', 'zoe@gmail.com', '6912', 'Adr12', '1986-02-12'),
(13, 'Thodoris', 'James', 'theojames@gmail.com', '6913', 'Adr13', '1993-03-13'),
(14, 'Fani', 'Fanou', 'fanika@gmail.com', '6914', 'Adr14', '1994-04-14'),
(15, 'Georgio', 'Giovani', 'giova@gmail.com', '6915', 'Adr15', '1978-05-15'),
(16, 'Anna', 'Triantafyllou', 'annerose@gmail.com', '6916', 'Adr16', '1989-06-16'),
(17, 'Stelios', 'Samaras', 'stsamaras12345@gmail.com', '6917', 'Adr17', '1996-07-17'),
(18, 'Ioanna', 'Alexiou', 'ionnaalexiou@gmail.com', '6918', 'Adr18', '2000-08-18'),
(19, 'Marifyllo', 'Kathariou', 'marifyllikathariou@gmail.com', '6919', 'Adr19', '1981-09-19'),
(20, 'Lia', 'Lilipouteia', 'lili@gmail.com', '6920', 'Adr20', '1990-10-20');

-- reservations
INSERT INTO reservation (res_tr_id, res_seatnum, res_cust_id, res_status, res_total_cost) VALUES 
(1, 1, 1, 'CONFIRMED', 500), (1, 2, 2, 'PAID', 500),
(1, 3, 3, 'PENDING', 500), (2, 1, 4, 'PAID', 600),
(2, 2, 5, 'CONFIRMED', 600), (3, 10, 6, 'PAID', 450),
(3, 11, 7, 'CANCELLED', 0), (4, 5, 8, 'PAID', 550),
(4, 6, 9, 'PAID', 550), (5, 1, 10, 'CANCELLED', 0),
(6, 15, 1, 'CONFIRMED', 700), (7, 4, 2, 'PENDING', 400),
(8, 1, 11, 'CONFIRMED', 800), (8, 2, 12, 'PAID', 800),
(9, 5, 13, 'PENDING', 350), (10, 1, 14, 'CONFIRMED', 380),
(11, 20, 15, 'PAID', 420), (11, 21, 16, 'PAID', 420),
(12, 1, 17, 'PENDING', 500), (13, 10, 18, 'CONFIRMED', 600),
(14, 2, 19, 'PAID', 450), (15, 5, 20, 'CONFIRMED', 950),
(1, 4, 11, 'CONFIRMED', 500), (2, 3, 12, 'PAID', 600),
(3, 12, 13, 'PENDING', 450);


INSERT INTO vehicles (ve_license_plate, ve_brand, ve_model, ve_seats, ve_type, ve_br_code) VALUES 
('ABC-1234', 'Mercedes', 'Sprinter', 20, 'MiniBus', 1), 
('XYZ-9876', 'Volvo', '9700', 50, 'Bus', 1),
('KAN-5555', 'Ford', 'Transit', 9, 'Van', 1),
('BIA-8796', 'Suzuki', 'WagonR', 5, 'Car', 2),
('ZME-3456', 'Citroen', 'C3', 5, 'Car', 2),
('ITR-1015', 'Nissan', 'Micra', 5, 'Car', 2),
('TEE-8945', 'Ford', 'Transit', 8 , 'Van', 2),
('AAO-9807', 'Renault', 'Cleo', 5, 'Car', 3),
('GIA-8934', 'Peugeot', 'Ε-EXPERT', 8 , 'Van', 3);

USE travel_agency;

INSERT INTO accommodation (
    acc_name, acc_type, acc_stars, acc_rating, acc_status, 
    acc_city, acc_street, acc_street_number, acc_zipcode, acc_phone, acc_email, 
    acc_total_rooms, acc_price_per_room, acc_facilities, acc_dst_id
) VALUES 
-- 1. Paris (HOTEL -> 5 αστέρια)
('Paris La Coeur', 'HOTEL', 5, 4.8, 'ACTIVE', 
 'Paris', 'Le Marais', 10, '75004', '33-1-0000', 'contact@lacoeur.fr', 
 100, 300.00, 'FREE WIFI,RESTAURANT / BAR,AC', 1),

-- 2. London (HOTEL -> 4 αστέρια)
('Kensington House', 'HOTEL', 4, 4.2, 'ACTIVE', 
 'London', 'Kensington High St', 20, 'W8 7NX', '44-20-0000', 'info@kensington.uk', 
 50, 200.00, 'FREE WIFI,AC', 2),

-- 3. Berlin (HOSTEL -> NULL αστέρια)
('Berlin Mitte Inn', 'HOSTEL', NULL, 4.3, 'ACTIVE', 
 'Berlin', 'Alexanderplatz', 5, '10178', '49-30-0000', 'stay@berlininn.de', 
 80, 30.00, 'FREE WIFI', 3),

-- 4. Rome (HOTEL -> 3 αστέρια)
('Roma Antiqua', 'HOTEL', 3, 4.0, 'ACTIVE', 
 'Rome', 'Via del Corso', 22, '00186', '39-06-0000', 'info@roma.it', 
 40, 120.00, 'AC', 4),

-- 5. Madrid (APARTMENT -> NULL αστέρια)
('Sol Plaza Apt', 'APARTMENT', NULL, 3.9, 'ACTIVE', 
 'Madrid', 'Puerta del Sol', 1, '28013', '34-91-0000', 'rent@sol.es', 
 15, 90.00, 'FREE WIFI,AC', 5),

-- 6. Chania (HOTEL -> 4 αστέρια)
('Venetian Harbor', 'HOTEL', 4, 4.6, 'ACTIVE', 
 'Chania', 'Old Port', 15, '73100', '30-28210', 'res@chania.gr', 
 60, 110.00, 'FREE WIFI,RESTAURANT / BAR', 6),

-- 10. Santorini (RESORT -> 5 αστέρια)
('Caldera Dream', 'RESORT', 5, 5.0, 'ACTIVE', 
 'Santorini', 'Oia Cliffs', 8, '84702', '30-22860', 'luxury@caldera.gr', 
 25, 500.00, 'FREE WIFI,AC,RESTAURANT / BAR,WHEELCHAIR ACCESS', 10);
 

INSERT INTO admin_dba (dba_username, dba_start_date, dba_end_date) 
VALUES 
('super_admin', '2020-01-01', NULL),      
('db_master', '2023-05-15', NULL),        
('junior_dba', '2024-01-10', NULL),       
('old_user', '2018-01-01', '2022-12-31'), 
('temp_fixer', '2023-08-01', '2023-08-30'); 



 
 












