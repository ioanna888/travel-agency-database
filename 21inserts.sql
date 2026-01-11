USE travel_agency_2025;

DELIMITER $$

-- triggers for trip
-- inserting
DROP TRIGGER IF EXISTS log_trip_insert$$
CREATE TRIGGER log_trip_insert AFTER INSERT ON trip
FOR EACH ROW

BEGIN

    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'INSERT', 'trip');
END$$

-- updating
DROP TRIGGER IF EXISTS log_trip_update$$

CREATE TRIGGER log_trip_update AFTER UPDATE ON trip
FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'UPDATE', 'trip');
END$$

-- deleting
DROP TRIGGER IF EXISTS log_trip_delete$$
CREATE TRIGGER log_trip_delete AFTER DELETE ON trip
FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'DELETE', 'trip');
END$$

-- triggers for table reservation
-- insert
DROP TRIGGER IF EXISTS log_reservation_insert$$

CREATE TRIGGER log_reservation_insert AFTER INSERT ON reservation

FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'INSERT', 'reservation');
END$$

-- update
DROP TRIGGER IF EXISTS log_reservation_update$$

CREATE TRIGGER log_reservation_update AFTER UPDATE ON reservation
FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'UPDATE', 'reservation');
END$$

-- delete 
DROP TRIGGER IF EXISTS log_reservation_delete$$
CREATE TRIGGER log_reservation_delete AFTER DELETE ON reservation
FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'DELETE', 'reservation');
END$$

-- customer
-- insert 
DROP TRIGGER IF EXISTS log_customer_insert$$

CREATE TRIGGER log_customer_insert AFTER INSERT ON customer

FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'INSERT', 'customer');
END$$

-- update
DROP TRIGGER IF EXISTS log_customer_update$$

CREATE TRIGGER log_customer_update AFTER UPDATE ON customer

FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'UPDATE', 'customer');
END$$

-- delete

DROP TRIGGER IF EXISTS log_customer_delete$$

CREATE TRIGGER log_customer_delete AFTER DELETE ON customer

FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'DELETE', 'customer');
END$$

-- destination
-- insert 
DROP TRIGGER IF EXISTS log_destination_insert$$

CREATE TRIGGER log_destination_insert AFTER INSERT ON destination

FOR EACH ROW

BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'INSERT', 'destination');
END$$

-- update 
DROP TRIGGER IF EXISTS log_destination_update$$

CREATE TRIGGER log_destination_update AFTER UPDATE ON destination

FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'UPDATE', 'destination');
END$$

-- deleting
DROP TRIGGER IF EXISTS log_destination_delete$$

CREATE TRIGGER log_destination_delete AFTER DELETE ON destination
FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'DELETE', 'destination');
END$$

-- oximate  
-- insert

DROP TRIGGER IF EXISTS log_vehicles_insert$$

CREATE TRIGGER log_vehicles_insert AFTER INSERT ON vehicles
FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'INSERT', 'vehicles');
END$$

-- update
DROP TRIGGER IF EXISTS log_vehicles_update$$

CREATE TRIGGER log_vehicles_update AFTER UPDATE ON vehicles
FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'UPDATE', 'vehicles');
END$$

-- deleting
DROP TRIGGER IF EXISTS log_vehicles_delete$$
CREATE TRIGGER log_vehicles_delete AFTER DELETE ON vehicles
FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'DELETE', 'vehicles');
END$$

-- accommodatin
-- inserting 
DROP TRIGGER IF EXISTS log_accommodation_insert$$

CREATE TRIGGER log_accommodation_insert AFTER INSERT ON accommodation

FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'INSERT', 'accommodation');
END$$

-- updating

DROP TRIGGER IF EXISTS log_accommodation_update$$

CREATE TRIGGER log_accommodation_update AFTER UPDATE ON accommodation
FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'UPDATE', 'accommodation');
END$$

-- deleting
DROP TRIGGER IF EXISTS log_accommodation_delete$$

CREATE TRIGGER log_accommodation_delete AFTER DELETE ON accommodation

FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'DELETE', 'accommodation');
END$$

-- trip accommodation
DROP TRIGGER IF EXISTS log_trip_accommodation_insert$$
CREATE TRIGGER log_trip_accommodation_insert AFTER INSERT ON trip_accommodation
FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'INSERT', 'trip_accommodation');
END$$

DROP TRIGGER IF EXISTS log_trip_accommodation_update$$
CREATE TRIGGER log_trip_accommodation_update AFTER UPDATE ON trip_accommodation
FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'UPDATE', 'trip_accommodation');
END$$

DROP TRIGGER IF EXISTS log_trip_accommodation_delete$$
CREATE TRIGGER log_trip_accommodation_delete AFTER DELETE ON trip_accommodation
FOR EACH ROW
BEGIN
    INSERT INTO system_logging (log_user, log_datetime, log_action, log_table_name)
    VALUES (USER(), NOW(), 'DELETE', 'trip_accommodation');
END$$

DELIMITER ;

-- allagi 
UPDATE customer SET cust_name = 'TestName' WHERE cust_id = 4;
SELECT * FROM system_logging;