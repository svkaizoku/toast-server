#Toast Database

DROP DATABASE toast;
CREATE DATABASE toast;
USE toast;

CREATE TABLE users (id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, first_name VARCHAR(100), last_name VARCHAR(100), username VARCHAR(100), email_id VARCHAR(100) CONSTRAINT email_unique UNIQUE (email_id));
CREATE TABLE channel (id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), description VARCHAR(50), start_time TIMESTAMP, CONSTRAINT name_unique UNIQUE (name));
CREATE TABLE channel_members(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, user_id INT, channel_id INT, FOREIGN KEY ch_fk_user_id(user_id) REFERENCES users(id), FOREIGN KEY fk_ch_id(channel_id) REFERENCES channel(id));

CREATE TABLE auth (user_id INT NOT NULL PRIMARY KEY, oauth VARCHAR(100), FOREIGN KEY auth_fk_user_id(user_id) REFERENCES users(id));