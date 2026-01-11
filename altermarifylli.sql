USE travel_agency_2025;

ALTER TABLE trip_accommodation
ADD COLUMN sa_rooms INT DEFAULT 1;

DESCRIBE trip_accommodation;