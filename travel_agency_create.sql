DROP DATABASE IF EXISTS travel_agency_2025;
CREATE DATABASE travel_agency_2025;
USE travel_agency_2025;

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

-- μου το προτεινε το gemini και ειναι κυκλικη εξαρτηση --
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