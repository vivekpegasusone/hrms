CREATE DATABASE IF NOT EXISTS tenant_master;

CREATE TABLE tenant_master.tenant_config (
    id bigint NOT NULL AUTO_INCREMENT,
    schema_name varchar(20) NOT NULL,
    domain varchar(100) NOT NULL,
    url varchar(255) NOT NULL,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    driver_class varchar(255) NOT NULL,
    is_active bit(1) NOT NULL,
    maximumpoolsize int NOT NULL,
    minimumidle int NOT NULL,
    maxlifetime int NOT NULL,
    keepalivetime int NOT NULL,
    connectiontimeout int NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO tenant_master.tenant_config (schema_name, domain, url, username, password, driver_class, 
is_active, maximumpoolsize, minimumidle, maxlifetime, keepalivetime, connectiontimeout) 
VALUES ('drishti', 'drishti.com', 'jdbc:mysql://localhost:3306/drishti?useSSL=false', 
'root', 'root', 'com.mysql.cj.jdbc.Driver', 1, 2, 2, 1800000, 120000, 30000);

INSERT INTO tenant_master.tenant_config (schema_name, domain, url, username, password, driver_class, 
is_active, maximumpoolsize, minimumidle, maxlifetime, keepalivetime, connectiontimeout) 
VALUES ('whizzy', 'whizzy.co.in', 'jdbc:mysql://localhost:3306/whizzy?useSSL=false', 
'root', 'root', 'com.mysql.cj.jdbc.Driver', 1, 2, 2, 1800000, 120000, 30000);

CREATE TABLE TENANT_CONFIG_USER (
    id bigint NOT NULL AUTO_INCREMENT,
    is_Active bit(1) NOT NULL,
    login_id varchar(50) NOT NULL,
    password_hash varchar(60) NOT NULL,
    first_name varchar(50) DEFAULT NULL,
    last_name varchar(50) DEFAULT NULL,
    email varchar(254) DEFAULT NULL,
    created_by varchar(50) NOT NULL,
    created_date datetime(6) DEFAULT NULL,
    updated_by varchar(50) DEFAULT NULL,
    updated_date datetime(6) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_TENANT_CONFIG_USER_LOGIN (login_id),
    UNIQUE KEY UK_TENANT_CONFIG_USER_EMAIL (email)
);

CREATE TABLE TENANT_CONFIG_AUTHORITY (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    created_by varchar(50) NOT NULL,
    created_date datetime(6) DEFAULT NULL,
    updated_by varchar(50) DEFAULT NULL,
    updated_date datetime(6) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_AUTHORITY_NAME (name)
);

CREATE TABLE TENANT_CONFIG_USER_AUTHORITY (
    user_id bigint NOT NULL,
    authority_id bigint NOT NULL,
    created_by varchar(50) NOT NULL,
    created_date datetime(6) DEFAULT NULL,
    updated_by varchar(50) DEFAULT NULL,
    updated_date datetime(6) DEFAULT NULL,
    PRIMARY KEY (user_id,authority_id),
    CONSTRAINT FK_TENANT_CONFIG_USER_AUTHORITY_ID FOREIGN KEY (authority_id) REFERENCES TENANT_CONFIG_AUTHORITY (id),
    CONSTRAINT FK_TENANT_CONFIG_USER_AUTHORITY_USER_ID FOREIGN KEY (user_id) REFERENCES TENANT_CONFIG_USER (id)
);


INSERT INTO TENANT_CONFIG_USER(is_Active,login_id,password_hash,first_name,last_name,email,created_by,
created_date,updated_by,updated_date)
VALUES(1,'ta1','pass','Amit','Tomar','amit@gmail.com','system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');

INSERT INTO TENANT_CONFIG_USER(is_Active,login_id,password_hash,first_name,last_name,email,created_by,
created_date,updated_by,updated_date)
VALUES(1,'ta2','pass','Viraj','Shah','viraj@gmail.com','system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');

INSERT INTO TENANT_CONFIG_USER(is_Active,login_id,password_hash,first_name,last_name,email,created_by,
created_date,last_modified_by,last_modified_date)
VALUES(1,'tu1','pass','Pallav','Mohan','pallav@gmail.com','system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');


INSERT INTO TENANT_CONFIG_AUTHORITY(name,created_by,created_date,updated_by,updated_date)VALUES('ADMIN','system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');
INSERT INTO TENANT_CONFIG_AUTHORITY(name,created_by,created_date,updated_by,updated_date)VALUES('USER','system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');

INSERT INTO TENANT_CONFIG_USER_AUTHORITY(user_id,authority_id,created_by,created_date,updated_by,updated_date)VALUES(1,1,'system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');
INSERT INTO TENANT_CONFIG_USER_AUTHORITY(user_id,authority_id,created_by,created_date,updated_by,updated_date)VALUES(2,1,'system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');
INSERT INTO TENANT_CONFIG_USER_AUTHORITY(user_id,authority_id,created_by,created_date,updated_by,updated_date)VALUES(2,2,'system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');
INSERT INTO TENANT_CONFIG_USER_AUTHORITY(user_id,authority_id,created_by,created_date,updated_by,updated_date)VALUES(3,2,'system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');


CREATE DATABASE IF NOT EXISTS drishti;

CREATE TABLE an_user (
    id bigint NOT NULL AUTO_INCREMENT,
    created_by varchar(50) NOT NULL,
    created_date datetime(6) DEFAULT NULL,
    last_modified_by varchar(50) DEFAULT NULL,
    last_modified_date datetime(6) DEFAULT NULL,
    activated bit(1) NOT NULL,
    activation_key varchar(20) DEFAULT NULL,
    email varchar(254) DEFAULT NULL,
    first_name varchar(50) DEFAULT NULL,
    image_url varchar(256) DEFAULT NULL,
    lang_key varchar(10) DEFAULT NULL,
    last_name varchar(50) DEFAULT NULL,
    login varchar(50) NOT NULL,
    password_hash varchar(60) NOT NULL,
    reset_date datetime(6) DEFAULT NULL,
    reset_key varchar(20) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_39u0kjc2jf7fxx2i3cgsilgi7 (login),
    UNIQUE KEY UK_n9q4y7xm208aka1osri5cu1kf (email)
);

CREATE TABLE an_authority (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_an_authority_name (name)
);

CREATE TABLE an_user_authority (
    user_id bigint NOT NULL AUTO_INCREMENT,
    authority_id bigint NOT NULL,
    PRIMARY KEY (user_id,authority_id),
    CONSTRAINT FKcmyw9fevws0ram3xg4luegigu FOREIGN KEY (authority_id) REFERENCES an_authority (id),
    CONSTRAINT FKoq7mr5hlasotcqb4sxq0x5bhk FOREIGN KEY (user_id) REFERENCES an_user (id)
);


INSERT INTO an_user(id,created_by,created_date,last_modified_by,last_modified_date,activated,activation_key,email,
first_name,last_name,image_url,lang_key,login,password_hash,reset_date,reset_key)
VALUES(1,'system','2023-01-01 00:00:00','system','2023-01-01 00:00:00',true,'','admin@localhost',
'Administrator','Administrator','','en','admin11','admin11','2023-01-01 00:00:00','');

INSERT INTO an_user(id,created_by,created_date,last_modified_by,last_modified_date,activated,activation_key,email,
first_name,last_name,image_url,lang_key,login,password_hash,reset_date,reset_key)
VALUES(2,'system','2023-01-01 00:00:00','system','2023-01-01 00:00:00',true,'','user@localhost',
'User','User','','en','user11','user11','2023-01-01 00:00:00','');

INSERT INTO an_authority(name)VALUES('ADMIN');
INSERT INTO an_authority(name)VALUES('USER');

INSERT INTO an_user_authority(user_id,authority_id)VALUES(1,1);
INSERT INTO an_user_authority(user_id,authority_id)VALUES(1,2);
INSERT INTO an_user_authority(user_id,authority_id)VALUES(2,2);

