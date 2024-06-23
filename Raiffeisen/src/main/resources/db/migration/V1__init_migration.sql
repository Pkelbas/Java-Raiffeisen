CREATE SEQUENCE accounts_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE accounts (
                          balance FLOAT(53) NOT NULL,
                          id SERIAL PRIMARY KEY,
                          user_id INTEGER,
                          account_name VARCHAR(255) NOT NULL,
                          bank_name VARCHAR(255) NOT NULL
);

CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       password VARCHAR(255),
                       role VARCHAR(255) CHECK (role IN ('USER')),
                       username VARCHAR(255) UNIQUE
);
