
CREATE DATABASE vstory;

CREATE USER 'vsadmin'@'localhost' identified by 'A$hl3y';

GRANT ALL PRIVILEGES ON vstory.* TO 'vsadmin'@'localhost'
IDENTIFIED BY 'A$hl3y';

CREATE USER 'vsread'@'localhost' identified by '$ydn3y';

GRANT SELECT on vstory.* to 'vsread'@'localhost' identified by '$ydn3y';
