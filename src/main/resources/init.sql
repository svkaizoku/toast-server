#Toast Database

DROP DATABASE toast;
CREATE DATABASE toast;
USE toast;

CREATE TABLE users (id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, first_name VARCHAR(20), last_tname VARCHAR(20), username VARCHAR(20));
CREATE TABLE channel (id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, name VARCHAR(50), description VARCHAR(50), start_time TIMESTAMP, CONSTRAINT name_unique UNIQUE (name));
CREATE TABLE channel_members(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, user_id INT, channel_id INT, FOREIGN KEY ch_fk_user_id(user_id) REFERENCES users(id), FOREIGN KEY fk_ch_id(channel_id) REFERENCES channel(id));

CREATE TABLE auth (user_id INT NOT NULL PRIMARY KEY, oauth VARCHAR(100), FOREIGN KEY auth_fk_user_id(user_id) REFERENCES users(id));