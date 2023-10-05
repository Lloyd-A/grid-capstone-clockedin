CREATE TABLE IF NOT EXISTS  people (id INT PRIMARY KEY, first_name VARCHAR(50));
MERGE INTO people (id, first_name) KEY (id) VALUES (1, 'Lloyd'), (2, 'Raheed'), (3, 'Allen'), (4, 'Gilzene');

CREATE TABLE IF NOT EXISTS  cars (vin_id INT PRIMARY KEY, make VARCHAR(50), model varchar(50));
MERGE INTO cars (vin_id, make, model) KEY(vin_id) VALUES (1, 'Honda', 'Integra'), (2, 'Honda', 'Civic'),
(3, 'Toyota', 'Supra');

CREATE TABLE IF NOT EXISTS  drivers (fk_person_id INT, fk_vin_id INT, PRIMARY KEY(fk_person_id, fk_vin_id),
FOREIGN KEY (fk_person_id) REFERENCES people(id), FOREIGN KEY (fk_vin_id) REFERENCES cars(vin_id));
MERGE INTO drivers (fk_person_id, fk_vin_id) KEY(fk_person_id, fk_vin_id) VALUES (1, 1), (1,3), (2, 2), (3,3);