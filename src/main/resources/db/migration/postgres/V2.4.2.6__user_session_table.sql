-- Creates nodejs passport session table

CREATE TABLE if NOT EXISTS session (
    sid VARCHAR(255) PRIMARY KEY,
    sess JSON NOT NULL,
    expire TIMESTAMP(6) NOT NULL
);