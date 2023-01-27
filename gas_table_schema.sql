CREATE DATABASE IF NOT EXISTS gas_tracker;

USE gas_tracker; -- following commands will execute in this database

CREATE TABLE fuel_prices(
	id int auto_increment primary key, -- autofill id column
    fuel_95_price decimal(6, 3), -- setting precision for decimal
    fuel_98_price decimal(6, 3),
    fuel_diesel_price decimal(6, 3),
    fuel_lpg_price decimal(6, 3),
    gas_station varchar(20),
    date_time datetime
);





