USE travel_agency;

ALTER TABLE trip 
ADD COLUMN tr_driver_AT CHAR(10),
ADD COLUMN tr_vehicle_license VARCHAR(20),
ADD CONSTRAINT fk_trip_driver FOREIGN KEY (tr_driver_AT) REFERENCES worker(wrk_AT),
ADD CONSTRAINT fk_trip_vehicle FOREIGN KEY (tr_vehicle_license) REFERENCES vehicles(ve_license_plate);

