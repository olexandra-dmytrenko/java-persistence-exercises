CREATE TABLE IF NOT EXISTS account (
    id BIGINT NOT NULL ,
    email varchar(255) NOT NULL ,
    first_name varchar(255) NOT NULL ,
    last_name varchar(255) NOT NULL ,
    gender varchar(255) NOT NULL ,
    birthday DATE NOT NULL ,
    balance DECIMAL(19,4) ,
    creation_time TIMESTAMP DEFAULT (now()) NOT NULL ,
    CONSTRAINT account_pk PRIMARY KEY (id),
    CONSTRAINT account_email_uq UNIQUE (email)
);