DROP DATABASE IF EXISTS travel_agency;
CREATE DATABASE travel_agency;
USE travel_agency;

CREATE TABLE branch (
	br_code int(11) NOT NULL,
    br_street varchar(50) ,
    br_num int(4) ,
    br_city varchar(30) ,
    br_manager_AT char(10),
    PRIMARY KEY (br_code)
);

CREATE TABLE worker (
	wrk_AT char(10),
    wrk_name varchar(30),
    wrk_lname varchar(30),
    wrk_email varchar(100),
    wrk_salary decimal(10,2),
    wrk_br_code int(11),
    PRIMARY KEY (wrk_AT),
	FOREIGN KEY (wrk_br_code) REFERENCES branch(br_code)
);

ALTER TABLE branch 
ADD CONSTRAINT fk_branch_manager 
FOREIGN KEY (br_manager_AT) REFERENCES worker(wrk_AT);

CREATE TABLE driver (
	drv_AT char(10),
	drv_license enum('A', 'B', 'C', 'D'),
    drv_route enum('LOCAL', 'ABROAD'),
    drv_experience tinyint(4),
	PRIMARY KEY (drv_AT),
	FOREIGN KEY (drv_AT) REFERENCES worker(wrk_AT) ON DELETE CASCADE
);

CREATE TABLE guide (
	gui_AT char(10) NOT NULL,
	gui_cv text,
	PRIMARY KEY (gui_AT),
	FOREIGN KEY (gui_AT) REFERENCES worker(wrk_AT) ON DELETE CASCADE
);

CREATE TABLE admin (
	adm_AT char(10) NOT NULL ,
    adm_type enum('LOGISTICS','ADMINISTRATIVE','ACCOUNTING'),
	adm_diploma varchar(200),
	PRIMARY KEY (adm_AT),
	FOREIGN KEY (adm_AT) REFERENCES worker(wrk_AT) ON DELETE CASCADE
);

CREATE TABLE language_ref (
	lang_code varchar(5) NOT NULL,
	lang_name varchar(50),
	PRIMARY KEY (lang_code)
);

CREATE TABLE languages (
	lng_gui_AT char(10) NOT NULL,
	lng_language_code varchar(5) NOT NULL,
	PRIMARY KEY (lng_gui_AT, lng_language_code),
	FOREIGN KEY (lng_gui_AT) REFERENCES guide(gui_AT),
	FOREIGN KEY (lng_language_code) REFERENCES language_ref(lang_code)
);

CREATE TABLE phones (
	ph_br_code int(11) NOT NULL,
    ph_number varchar(15) NOT NULL,
    PRIMARY KEY (ph_br_code, ph_number),
    FOREIGN KEY (ph_br_code) REFERENCES branch(br_code)
);

CREATE TABLE manages (
	mng_adm_AT char(10) NOT NULL,
    mng_br_code INT(11) NOT NULL,
    PRIMARY KEY (mng_adm_AT, mng_br_code ),
    FOREIGN KEY (mng_adm_AT) REFERENCES admin(adm_AT),
	FOREIGN KEY (mng_br_code) REFERENCES branch(br_code)
);

CREATE TABLE trip (
	tr_id int(11) NOT NULL,
	tr_departure datetime,
	tr_return datetime,
	tr_maxseats tinyint(4),
	tr_cost_adult decimal(10,2),
	tr_cost_child decimal(10,2),
	tr_status enum('PLANNED','CONFIRMED','ACTIVE','COMPLETED','CANCELLED'),
	tr_min_participants tinyint(4),
	tr_br_code int(11),
	tr_gui_AT char(10),
	tr_drv_AT char(10),
	PRIMARY KEY (tr_id),
	FOREIGN KEY (tr_br_code) REFERENCES branch(br_code),
	FOREIGN KEY (tr_gui_AT) REFERENCES guide(gui_AT),
	FOREIGN KEY (tr_drv_AT) REFERENCES driver(drv_AT)
);

CREATE TABLE event (
	ev_tr_id int(11) NOT NULL,
    ev_start datetime NOT NULL,
	ev_end datetime,
	ev_descr text,
	PRIMARY KEY (ev_tr_id, ev_start), -- Υποθέτουμε σύνθετο κλειδί ή μόνο ev_tr_id αν είναι 1-1
	FOREIGN KEY (ev_tr_id) REFERENCES trip(tr_id)
);

CREATE TABLE destination (
	dst_id int(11) NOT NULL,
	dst_name varchar(100),
	dst_descr text,
	dst_rtype enum('LOCAL','ABROAD'),
	dst_language_code varchar(5),
	dst_location int(11),
	PRIMARY KEY (dst_id),
	FOREIGN KEY (dst_language_code) REFERENCES language_ref(lang_code)
);

CREATE TABLE travel_to (
	to_tr_id int(11) NOT NULL,
	to_dst_id int(11) NOT NULL,
	to_arrival datetime,
	to_departure datetime,
	PRIMARY KEY (to_tr_id, to_dst_id),
	FOREIGN KEY (to_tr_id) REFERENCES trip(tr_id),
	FOREIGN KEY (to_dst_id) REFERENCES destination(dst_id)
);

CREATE TABLE customer (
	cust_id int(11) NOT NULL,
	cust_name varchar(30),
	cust_lname varchar(30),
	cust_email varchar(100),
	cust_phone varchar(15),
	cust_address text,
	cust_birth_date date,
	PRIMARY KEY (cust_id)
);

CREATE TABLE reservation (
	res_tr_id int(11) NOT NULL,
	res_seatnum tinyint(4) NOT NULL,
	res_cust_id int(11) NOT NULL,
	res_status enum('PENDING','CONFIRMED','PAID','CANCELLED'),
	res_total_cost decimal(10,2),
	PRIMARY KEY (res_tr_id, res_seatnum), 
	FOREIGN KEY (res_tr_id) REFERENCES trip(tr_id),
	FOREIGN KEY (res_cust_id) REFERENCES customer(cust_id)
);

CREATE TABLE vehicles (
	ve_id INT auto_increment PRIMARY KEY ,
    ve_brand VARCHAR(30) NOT NULL,
    ve_model VARCHAR(30) NOT NULL, 
    ve_license_plate VARCHAR(20) NOT NULL UNIQUE,
    ve_seats INT NOT NULL,
    ve_km INT DEFAULT 0,
    ve_type ENUM('Bus', 'MiniBus', 'Van', 'Car') NOT NULL,
    ve_status ENUM ('Available', 'In use', 'Currently being repaired') NOT NULL,
    ve_br_code INT(11) NOT NULL,
    FOREIGN KEY (ve_br_code) REFERENCES branch(br_code) ON DELETE CASCADE ON UPDATE CASCADE -- σε ποιο branch ειναι 
);


CREATE TABLE IF NOT EXISTS accommodation(
	acc_id INT auto_increment PRIMARY KEY,
    acc_name VARCHAR(100) NOT NULL,
    acc_type ENUM('HOTEL', 'HOSTEL', 'RESORT', 'APARTMENT' , 'ROOM') NOT NULL,
    acc_rating DECIMAL(3,2) DEFAULT 0.00,
    acc_stars INT ,
    acc_status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    acc_city VARCHAR(50) NOT NULL,
    acc_street VARCHAR(50) NOT NULL,
    acc_street_number INT NOT NULL,
    acc_zipcode VARCHAR(100),
    acc_phone VARCHAR(100),
    acc_email VARCHAR(100),
    acc_total_rooms INT NOT NULL,
    acc_price_per_room DECIMAL(10,2) NOT NULL,
    acc_facilities SET('FREE WIFI', 'RESTAURANT / BAR', 'AC' , 'WHEELCHAIR ACCESS'),
    acc_dst_id INT(11) NOT NULL ,
    FOREIGN KEY (acc_dst_id) REFERENCES destination(dst_id) ON DELETE CASCADE ON UPDATE CASCADE,
    
    CONSTRAINT chk_stars_logic CHECK (
        ((acc_type IN ('Hotel', 'Resort')) AND (acc_stars BETWEEN 1 AND 5)) OR 
        ((acc_type NOT IN ('Hotel', 'Resort')) AND (acc_stars IS NULL))
    )
);


CREATE TABLE IF NOT EXISTS trip_accommodation (
    trip_id INT NOT NULL,
    acc_id INT NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    -- Υπολογισμός: (Ημ.Αναχώρησης - Ημ.Άφιξης)
    nights INT GENERATED ALWAYS AS (DATEDIFF(check_out, check_in)) STORED, 
    cost DECIMAL(10, 2) DEFAULT 0.00, 
    PRIMARY KEY (trip_id, acc_id),
    FOREIGN KEY (trip_id) REFERENCES trip(tr_id) ON DELETE CASCADE,
    FOREIGN KEY (acc_id) REFERENCES accommodation(acc_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS admin_dba (
	dba_id INT auto_increment PRIMARY KEY,
    dba_username VARCHAR(50) NOT NULL UNIQUE,
    dba_start_date DATE NOT NULL,
    dba_end_date DATE DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS system_logging(
	log_id INT auto_increment PRIMARY KEY,
    log_user VARCHAR(50) NOT NULL,
    log_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
    log_action ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
	log_table_name VARCHAR(50) NOT NULL
);

ALTER TABLE trip 
ADD COLUMN tr_driver_AT CHAR(10),
ADD COLUMN tr_vehicle_license VARCHAR(20),
ADD CONSTRAINT fk_trip_driver FOREIGN KEY (tr_driver_AT) REFERENCES worker(wrk_AT),
ADD CONSTRAINT fk_trip_vehicle FOREIGN KEY (tr_vehicle_license) REFERENCES vehicles(ve_license_plate);

-- 3.1.3.1 & ΓΕΝΙΚΑ 
ALTER TABLE trip_accommodation 
ADD COLUMN sa_rooms INT DEFAULT 1;

ALTER TABLE trip DROP FOREIGN KEY fk_trip_vehicle;

ALTER TABLE trip DROP INDEX fk_trip_vehicle;

-- ΧΡΕΙΑΣΤΗΚΕ ΓΙΑ 3.1.3.1
ALTER TABLE trip 
ADD COLUMN tr_ve_id INT DEFAULT NULL, 
ADD CONSTRAINT fk_trip_vehicle 
FOREIGN KEY (tr_ve_id) REFERENCES vehicles(ve_id) 
ON UPDATE CASCADE ON DELETE SET NULL;

-- ΧΡΕΙΑΣΤΗΚΕ ΓΙΑ 3.1.3.3
ALTER TABLE trip ADD COLUMN tr_participants INT DEFAULT 0;

-- 3.1.3.4

ALTER TABLE trip 
ADD COLUMN tr_return_km INT DEFAULT 0;








