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
    created_on datetime(6) DEFAULT NULL,
    updated_by varchar(50) DEFAULT NULL,
    updated_on datetime(6) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_TENANT_CONFIG_USER_LOGIN (login_id),
    UNIQUE KEY UK_TENANT_CONFIG_USER_EMAIL (email)
);

CREATE TABLE TENANT_CONFIG_AUTHORITY (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    created_by varchar(50) NOT NULL,
    created_on datetime(6) DEFAULT NULL,
    updated_by varchar(50) DEFAULT NULL,
    updated_on datetime(6) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_AUTHORITY_NAME (name)
);

CREATE TABLE TENANT_CONFIG_USER_AUTHORITY (
    user_id bigint NOT NULL,
    authority_id bigint NOT NULL,
    created_by varchar(50) NOT NULL,
    created_on datetime(6) DEFAULT NULL,
    updated_by varchar(50) DEFAULT NULL,
    updated_on datetime(6) DEFAULT NULL,
    PRIMARY KEY (user_id,authority_id),
    CONSTRAINT FK_TENANT_CONFIG_USER_AUTHORITY_ID FOREIGN KEY (authority_id) REFERENCES TENANT_CONFIG_AUTHORITY (id),
    CONSTRAINT FK_TENANT_CONFIG_USER_AUTHORITY_USER_ID FOREIGN KEY (user_id) REFERENCES TENANT_CONFIG_USER (id)
);


INSERT INTO TENANT_CONFIG_USER(is_Active,login_id,password_hash,first_name,last_name,email,created_by,
created_on,updated_by,updated_on)
VALUES(1,'ta1','pass','Amit','Tomar','amit@gmail.com','system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');

INSERT INTO TENANT_CONFIG_USER(is_Active,login_id,password_hash,first_name,last_name,email,created_by,
created_on,updated_by,updated_on)
VALUES(1,'ta2','pass','Viraj','Shah','viraj@gmail.com','system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');

INSERT INTO TENANT_CONFIG_USER(is_Active,login_id,password_hash,first_name,last_name,email,created_by,
created_on,updated_by,updated_on)
VALUES(1,'tu1','pass','Pallav','Mohan','pallav@gmail.com','system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');


INSERT INTO TENANT_CONFIG_AUTHORITY(name,created_by,created_on,updated_by,updated_on)VALUES('ADMIN','system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');
INSERT INTO TENANT_CONFIG_AUTHORITY(name,created_by,created_on,updated_by,updated_on)VALUES('USER','system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');

INSERT INTO TENANT_CONFIG_USER_AUTHORITY(user_id,authority_id,created_by,created_on,updated_by,updated_on)VALUES(1,1,'system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');
INSERT INTO TENANT_CONFIG_USER_AUTHORITY(user_id,authority_id,created_by,created_on,updated_by,updated_on)VALUES(2,1,'system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');
INSERT INTO TENANT_CONFIG_USER_AUTHORITY(user_id,authority_id,created_by,created_on,updated_by,updated_on)VALUES(2,2,'system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');
INSERT INTO TENANT_CONFIG_USER_AUTHORITY(user_id,authority_id,created_by,created_on,updated_by,updated_on)VALUES(3,2,'system','2023-10-18 00:00:00','system','2023-10-18 00:00:00');


CREATE DATABASE IF NOT EXISTS drishti;
CREATE DATABASE IF NOT EXISTS whizzy;
--Below queries need to be run for both schema, insert data may be changed.

CREATE TABLE user (
    id bigint NOT NULL AUTO_INCREMENT,
	login_id varchar(100) NOT NULL,
	password_hash varchar(100) NOT NULL,
    first_name varchar(50) NOT NULL,
    last_name varchar(50) DEFAULT NULL,    
	designation_id bigint DEFAULT NULL,
	role_id bigint DEFAULT NULL,
	reporting_manager_id bigint DEFAULT NULL,
	hr_manager_id bigint DEFAULT NULL,	
	department_id bigint DEFAULT NULL,
	devision_id bigint DEFAULT NULL,
	account_id bigint DEFAULT NULL,
	policy_group_id bigint DEFAULT NULL,	
	status bit(1) NOT NULL,
	user_code varchar(20) DEFAULT NULL,
	email varchar(100) NOT NULL,
	date_of_joining datetime NOT NULL,
	date_of_relieving datetime DEFAULT NULL,
	data_of_birth datetime NOT NULL,
	date_of_marriage datetime DEFAULT NULL,
	gender varchar(1) DEFAULT 'M',
	created_by varchar(50) NOT NULL,
    created_on datetime NOT NULL,
    updated_by varchar(50) NOT NULL,
    updated_on datetime NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_login_id (login_id),
    UNIQUE KEY uk_user_email (email)
);

CREATE TABLE access_group (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(50) NOT NULL,
	status bit(1) NOT NULL,
	created_by varchar(50) NOT NULL,
    created_on datetime NOT NULL,
    updated_by varchar(50) NOT NULL,
    updated_on datetime NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_access_group_name (name)
);

CREATE TABLE user_access_group (
    user_id bigint NOT NULL,
    access_group_id bigint NOT NULL,
    created_by varchar(50) NOT NULL,
    created_on datetime NOT NULL,
    updated_by varchar(50) NOT NULL,
    updated_on datetime NOT NULL,
    PRIMARY KEY (user_id, access_group_id),
    CONSTRAINT fk_user_access_group_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_user_access_group_access_group FOREIGN KEY (access_group_id) REFERENCES access_group (id)
);
	
INSERT INTO user(login_id, password_hash, first_name, last_name, status, email, date_of_joining, data_of_birth, 
created_by, created_on, updated_by, updated_on)
VALUES('system', 'system', 'system', 'system', false, 'system@localhost', '2023-10-20', '2023-10-20', 
'system', '2023-10-20', 'system', '2023-10-20');

INSERT INTO user(login_id, password_hash, first_name, last_name, status, email, date_of_joining, data_of_birth,
created_by, created_on, updated_by, updated_on)
VALUES('vivek@drishti.com', 'secret', 'Vivek', 'Singh', true, 'vivek@drishti.com', '2023-10-20', '2023-10-20', 
'system' , '2023-10-20', 'system', '2023-10-20');

INSERT INTO access_group(name, status, created_by, created_on, updated_by, updated_on)
VALUES('ADMIN', 1, 'system', '2023-10-20', 'system', '2023-10-20');

INSERT INTO user_access_group(user_id, access_group_id, created_by, created_on, updated_by, updated_on)
VALUES(2, 1, 'system', '2023-10-20', 'system', '2023-10-20');


