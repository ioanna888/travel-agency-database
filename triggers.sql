USE travel_agency;

-- 3.1.4.2)

DELIMITER $
DROP TRIGGER IF EXISTS trigger_cost_calculation $

CREATE TRIGGER trigger_cost_calculation
BEFORE INSERT ON trip_accommodation
FOR EACH ROW
BEGIN 
	DECLARE v_price DECIMAL(10,2);
    DECLARE v_nights INT;
    
    SELECT acc_price_per_room 
    INTO v_price
    FROM accommodation
    WHERE acc_id = NEW.acc_id;
    
    -- υπολογιζω τισ διανυκτερευσεις
    SET v_nights = DATEDIFF(NEW.check_out, NEW.check_in);
    
    -- υπολογιζω το συνολικο κοστοσ τιμη * διανυκτερευσεις * δωματια 
    SET NEW.cost = v_price * v_nights * IFNULL(NEW.sa_rooms, 1);
    
    END$


-- 3.1.4.3)

DROP TRIGGER IF EXISTS update_vehicle_after_end_of_trip $

CREATE TRIGGER update_vehicle_after_end_of_trip
AFTER UPDATE ON trip
FOR EACH ROW
BEGIN
	IF NEW.tr_status = 'COMPLETED' AND OLD.tr_status != 'COMPLETED' 
    AND NEW.tr_ve_id IS NOT NULL THEN
    
    UPDATE vehicles
    SET 
		ve_km = ve_km + IFNULL(NEW.tr_return_km, 0),
        ve_status = 'Available'
        WHERE ve_id = NEW.tr_ve_id;
		END IF;
        
END$

DELIMITER ;



        


    
    