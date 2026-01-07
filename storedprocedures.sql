USE travel_agency;

DELIMITER $
DROP PROCEDURE IF EXISTS vehicle_assignment$

CREATE PROCEDURE vehicle_assignment (
	IN pr_trip_id INT,
    IN pr_veh_license VARCHAR(20),
    IN pr_current_km INT 
)

BEGIN
	DECLARE v_status ENUM ('Available', 'In use', 'Currently being repaired');
    DECLARE v_type ENUM ('Bus', 'MiniBus', 'Van', 'Car');
    DECLARE v_license_type ENUM ('A', 'B', 'C', 'D');
    DECLARE v_ve_id INT;
    DECLARE v_seats INT;
    DECLARE v_old_km INT;
    DECLARE v_drv_at CHAR(10);
    DECLARE v_departure DATETIME;
    DECLARE v_return DATETIME;
    DECLARE v_reservations INT;
    DECLARE v_overlap INT DEFAULT 0;
    
    -- Στοιχεια που αφορούν το όχημα
    SELECT ve_status, ve_type, ve_id, ve_seats,ve_km
    INTO  v_status, v_type, v_ve_id,v_seats, v_old_km
    FROM vehicles
    WHERE  ve_license_plate = pr_veh_license;
    
    -- Στοιχεία που αφορούν το ταξίδι 
    SELECT tr_drv_AT, tr_departure, tr_return
    INTO v_drv_at, v_departure, v_return
    FROM trip
    WHERE tr_id = pr_trip_id;
    
    -- Στοιχεία που αφορούν το δίπλωμα του οδηγού 
    SELECT drv_license
    INTO v_license_type
    FROM driver
    WHERE drv_AT = v_drv_at;
    
    SELECT COUNT(*) 
    INTO v_reservations
    FROM reservation
    WHERE res_tr_id = pr_trip_id AND res_status IN ('CONFIRMED', 'PAID');
    
    -- ΕΛΕΓΧΟΙ 
    IF v_ve_id IS NULL THEN 
		SELECT 'Error! Vehicle not found' AS Message;
        
	ELSEIF pr_current_km < v_old_km THEN
        SELECT 'Error! New mileage cannot be lower than current mileage' AS Message;
	
    ELSEIF v_status != 'Available' THEN
        SELECT 'Error! Vehicle is currently not Available' AS Message;
        
	ELSEIF v_seats < v_reservations THEN
        SELECT 'Error! Not enough seats for current reservations' AS Message;
        
	ELSEIF (v_seats > 9 AND v_license_type NOT IN ('C', 'D')) OR 
           (v_type = 'Car' AND v_license_type = 'A') THEN
        SELECT 'Error! Driver does not have the appropriate license' AS Message;
        
	ELSE
        -- Έλεγχος Επικάλυψης
        SELECT COUNT(*) INTO v_overlap
        FROM trip
        WHERE tr_ve_id = v_ve_id 
          AND tr_id != pr_trip_id 
          AND (
              (tr_departure BETWEEN v_departure AND v_return) OR 
              (tr_return BETWEEN v_departure AND v_return) OR
              (v_departure BETWEEN tr_departure AND tr_return)
          );

        IF v_overlap > 0 THEN
            SELECT 'Error! Vehicle is assigned to another trip during this period' AS Message;
	
  ELSE
            START TRANSACTION;
            UPDATE vehicles SET ve_km = pr_current_km, ve_status = 'In use' WHERE ve_id = v_ve_id;
            UPDATE trip SET tr_ve_id = v_ve_id WHERE tr_id = pr_trip_id;
            COMMIT;
            
            SELECT 'Success! Vehicle assigned successfully' AS Message;
        END IF;
    END IF;
END$


-- 3.1.3.2


DROP PROCEDURE IF EXISTS FindingAvailableAccommodation$

CREATE PROCEDURE FindingAvailableAccommodation(
    IN p_destination_id INT,
    IN p_check_in DATE,
    IN p_check_out DATE,
    IN p_rooms_needed INT,
    OUT p_first_accommodation_id INT
)
BEGIN
    SET p_first_accommodation_id = NULL;

    -- 1. ΛΟΓΙΚΗ: Εύρεση ID (Διορθωμένο p_date_out -> p_check_out)
    SELECT a.acc_id INTO p_first_accommodation_id
    FROM accommodation a
    LEFT JOIN trip_accommodation ta ON a.acc_id = ta.acc_id 
        AND (ta.check_in < p_check_out AND ta.check_out > p_check_in) -- Διόρθωση εδώ

    LEFT JOIN trip t ON ta.trip_id = t.tr_id
    WHERE a.acc_dst_id = p_destination_id AND a.acc_status = "ACTIVE"
    AND (t.tr_status != "CANCELLED" OR t.tr_status IS NULL)
    
    GROUP BY a.acc_id
    -- Διόρθωση Group By με MAX()
    HAVING (MAX(a.acc_total_rooms) - IFNULL(SUM(ta.sa_rooms), 0)) >= p_rooms_needed
    ORDER BY a.acc_price_per_room ASC, a.acc_stars DESC, a.acc_rating DESC
    LIMIT 1;

    -- 2. ΕΜΦΑΝΙΣΗ (Αν το ζητάει η άσκηση να φαίνεται και εδώ)
    -- Πρέπει να διορθωθεί ΚΑΙ ΕΔΩ το p_date_out
    SELECT
        a.acc_name AS "COMPANY NAME",
        a.acc_type AS "TYPE",
        CONCAT(a.acc_street, ' ', a.acc_street_number, ', ', a.acc_city) AS "ADDRESS",
        a.acc_phone AS "PHONE NUMBER",
        a.acc_stars AS "STARS",
        a.acc_rating AS "RATING",
        a.acc_price_per_room AS "PRICE/ROOM",
        a.acc_facilities AS "FACILITIES",
        (MAX(a.acc_total_rooms) - IFNULL(SUM(ta.sa_rooms), 0)) AS free_rooms

    FROM accommodation a
    LEFT JOIN trip_accommodation ta ON a.acc_id = ta.acc_id
        AND (ta.check_in < p_check_out AND ta.check_out > p_check_in) -- Διόρθωση εδώ

    LEFT JOIN trip t ON ta.trip_id = t.tr_id 
    WHERE a.acc_dst_id = p_destination_id
    AND a.acc_status = "ACTIVE"
    AND (t.tr_status != "CANCELLED" OR t.tr_status IS NULL)
    
    GROUP BY a.acc_id
    HAVING free_rooms >= p_rooms_needed
    ORDER BY a.acc_price_per_room ASC, a.acc_stars DESC, a.acc_rating DESC;

END$


-- 3.1.3.3)

DROP PROCEDURE IF EXISTS BookTripAccommodation $

CREATE PROCEDURE BookTripAccommodation(
    IN p_trip_id INT
)
BEGIN
	DECLARE v_finished INT DEFAULT 0;
    DECLARE v_dst_id INT;
    DECLARE v_check_in DATE;
    DECLARE v_check_out DATE;
    DECLARE v_participants INT;
    DECLARE v_rooms_needed INT;
    DECLARE v_found_acc_id INT;
    DECLARE v_error_occurred BOOLEAN DEFAULT FALSE;
    
    DECLARE cur_destination CURSOR FOR
		SELECT to_dst_id, DATE(to_arrival), DATE(to_departure)
        FROM travel_to
        WHERE to_tr_id = p_trip_id;
	
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished=1;
    
    SELECT tr_participants INTO v_participants 
    FROM trip WHERE tr_id = p_trip_id;
    
    -- υπολογισμος δωματίων 
    SET v_rooms_needed = CEILING(IFNULL(v_participants, 0) / 2);
    IF v_rooms_needed = 0 THEN SET v_rooms_needed = 1; END IF;
    
    START TRANSACTION ;
    OPEN cur_destination;
    
    read_loop: LOOP
		FETCH cur_destination INTO v_dst_id, v_check_in, v_check_out;
        
        IF v_finished = 1 THEN
			LEAVE read_loop;
		END IF;
        
        SET v_found_acc_id = NULL;
        
        CALL FindingAvailableAccommodation(
            v_dst_id, 
            v_check_in, 
            v_check_out, 
            v_rooms_needed, 
            v_found_acc_id
        );
        
        IF v_found_acc_id IS NULL THEN
            SET v_error_occurred = TRUE;
            LEAVE read_loop;
		
        ELSE
            -- INSERT
            -- Στέλνουμε τις ημερομηνίες check_in/check_out για να δουλέψει ο Trigger
            INSERT INTO trip_accommodation (trip_id, acc_id, sa_rooms, check_in, check_out)
            VALUES (p_trip_id, v_found_acc_id, v_rooms_needed, v_check_in, v_check_out);
        END IF;
        END LOOP;

    CLOSE cur_destination;
    
    IF v_error_occurred THEN
        ROLLBACK; 
        SELECT 'Αποτυχία: Δεν βρέθηκε κατάλυμα για κάποιον προορισμό. Ακυρώθηκαν όλες οι κρατήσεις.' AS Message;
    ELSE
        COMMIT;
    
        SELECT 
            a.acc_name AS "Accommodation",
            ta.check_in AS "Check In",
            ta.check_out AS "Check Out",
            DATEDIFF(ta.check_out, ta.check_in) AS "Nights",
            ta.sa_rooms AS "Rooms",
            ta.cost AS "Cost"
		FROM trip_accommodation ta
        JOIN accommodation a ON ta.acc_id = a.acc_id
        WHERE ta.trip_id = p_trip_id;
		
        SELECT SUM(price) AS "Total Trip Accommodation Cost"
        FROM trip_accommodation
        WHERE trip_id = p_trip_id;
    END IF;
    
    END $
    
DELIMITER ;


    
    
    

