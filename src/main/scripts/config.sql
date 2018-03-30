CREATE DATABASE dev;
CREATE DATABASE prod;

CREATE USER 'dev_user'@'localhost' IDENTIFIED BY 'dev';
CREATE USER 'prod_user'@'localhost' IDENTIFIED BY  'prod';

GRANT SELECT , INSERT , DELETE , UPDATE ON dev.* to 'dev_user'@'localhost';
GRANT SELECT , INSERT , DELETE , UPDATE ON prod.* to 'prod_user'@'localhost';

