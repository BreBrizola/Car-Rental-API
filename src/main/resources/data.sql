CREATE TABLE location (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    opening_hours VARCHAR(255) NOT NULL,
    after_hours_fee DOUBLE NOT NULL
);

CREATE TABLE vehicle (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    location_id BIGINT,
    FOREIGN KEY (location_id) REFERENCES location(id)
);

CREATE TABLE additional_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    vehicle_id BIGINT,
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
);

CREATE TABLE reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255) NOT NULL,
    confirmation_number VARCHAR(10) NOT NULL,
    total_price DOUBLE NOT NULL,
    pickup_location_id BIGINT,
    return_location_id BIGINT,
    vehicle_id BIGINT,
    pickup_date DATE,
    return_date DATE,
    pickup_time VARCHAR(255),
    return_time VARCHAR(255),
    FOREIGN KEY (pickup_location_id) REFERENCES location(id),
    FOREIGN KEY (return_location_id) REFERENCES location(id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicle(id)
);

CREATE TABLE login (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE profile (
                         loyalty_number VARCHAR(255) PRIMARY KEY,
                         first_name VARCHAR(255) NOT NULL,
                         last_name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL,
                         phone VARCHAR(255) NOT NULL,
                         date_of_birth DATE,
                         city VARCHAR(255),
                         country VARCHAR(255),
                         country_subdivision VARCHAR(255),
                         postal VARCHAR(255),
                         street_addresses VARCHAR(255),
                         country_code VARCHAR(255),
                         country_subdivision_code VARCHAR(255),
                         drivers_license_number VARCHAR(255),
                         drivers_license_state VARCHAR(255),
                         drivers_license_expiry DATE,
                         login_id BIGINT,
                         FOREIGN KEY (login_id) REFERENCES login(id)
);

--Inserts
INSERT INTO location (name, address, opening_hours, after_hours_fee)
VALUES
    ('ZoomCar Rentals', 'Rua das Flores, 750, Belo Horizonte, MG, 30110-010', '07:00 - 19:00', 40.00),
    ('EasyRide Car Rentals', 'Av. Beira Mar, 1500, Fortaleza, CE, 60165-121', '08:00 - 22:00', 50.00),
    ('AutoGo Car Hire', 'Rua dos Andradas, 900, Porto Alegre, RS, 90020-007', '09:00 - 21:00', 45.00),
    ('TopGear Rentals', 'Av. Brasil, 500, Recife, PE, 50030-000', '06:00 - 20:00', 35.00),
    ('Prime Wheels Car Rentals', 'Rua do Comércio, 300, Salvador, BA, 40010-000', '07:30 - 19:30', 55.00),
    ('MetroCar Rentals', 'Av. das Nações Unidas, 12555, São Paulo, SP, 04578-903', '08:00 - 20:00', 60.00),
    ('SpeedyRentals', 'Rua Voluntários da Pátria, 2000, Rio de Janeiro, RJ, 22270-000', '09:00 - 19:00', 50.00),
    ('EcoDrive Rentals', 'Av. Tancredo Neves, 1500, Salvador, BA, 41820-021', '07:00 - 21:00', 40.00),
    ('DriveEasy Car Rentals', 'Av. Paulista, 1000, São Paulo, SP, 01310-100', '08:00 - 20:00', 50.00),
    ('FastTrack Rentals', 'R. Augusta, 2500, São Paulo, SP, 01412-000', '07:00 - 19:00', 45.00),
    ('UrbanDrive Car Hire', 'Av. Rio Branco, 120, Rio de Janeiro, RJ, 20040-001', '09:00 - 21:00', 40.00),
    ('GreenRide Rentals', 'R. XV de Novembro, 300, Curitiba, PR, 80060-000', '06:00 - 22:00', 35.00),
    ('CityWheels Rentals', 'Av. Infante Dom Henrique, 85, Rio de Janeiro, RJ, 20021-140', '10:00 - 18:00', 55.00);

INSERT INTO vehicle (model, price, location_id)
VALUES
    ('Fiat Uno', 120, 1),
    ('Toyota Corolla', 200, 1),
    ('Hyundai HB20', 150, 2),
    ('Ford Mustang', 250, 2),
    ('Chevrolet Onix', 100, 3),
    ('BMW X5', 300, 3),
    ('Renault Kwid', 110, 4),
    ('Jeep Renegade', 210, 4),
    ('Volkswagen Gol', 90, 5),
    ('Honda HR-V', 180, 5),
    ('Nissan Sentra', 160, 6),
    ('Audi A3', 260, 6),
    ('Ford Ka', 115, 7),
    ('Volkswagen Tiguan', 195, 7),
    ('Mercedes-Benz C-Class', 320, 8),
    ('Fiat Mobi', 105, 8);

INSERT INTO additional_product (name, price, vehicle_id)
VALUES
    ('GPS', 10, 1),
    ('Radio', 10, 1),
    ('Child Seat', 15, 2),
    ('Extra Luggage Rack', 20, 2),
    ('Wi-Fi Hotspot', 12, 3),
    ('Roadside Assistance', 8, 3),
    ('GPS', 10, 4),
    ('Sunroof Cover', 18, 4),
    ('Child Seat', 15, 5),
    ('Extra Luggage Rack', 20, 5),
    ('Wi-Fi Hotspot', 12, 6),
    ('Radio', 10, 6),
    ('GPS', 10, 7),
    ('Roadside Assistance', 8, 7),
    ('Child Seat', 15, 8),
    ('Sunroof Cover', 18, 8);

COMMIT;
--http://localhost:8090/h2-console