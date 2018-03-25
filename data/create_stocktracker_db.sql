CREATE DATABASE stocktracker;
use stocktracker;
CREATE USER 'stocktracker'@'localhost' IDENTIFIED BY 'usarmy84';
GRANT ALL PRIVILEGES ON * . * TO 'stocktracker'@'localhost';
FLUSH PRIVILEGES;