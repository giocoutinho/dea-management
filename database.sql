CREATE TABLE user (
	id INTEGER auto_increment NOT NULL,
	name varchar(256) NOT NULL,
	email varchar(256) NOT NULL,
	password varchar(100) NOT NULL,
	linkedin varchar(128) NULL,
	CONSTRAINT user_PK PRIMARY KEY (id)
);