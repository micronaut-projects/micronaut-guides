-- Executed by the Oracle container entrypoint as SYSDBA.
-- The Testcontainers JDBC URL points to FREEPDB1, so create the schema there
-- instead of in the container database root.
WHENEVER SQLERROR EXIT SQL.SQLCODE

ALTER SESSION SET CONTAINER = FREEPDB1;

CREATE USER hr NO AUTHENTICATION
    DEFAULT TABLESPACE users
    QUOTA UNLIMITED ON users;

CREATE TABLE hr.employees (
    employee_id NUMBER PRIMARY KEY,
    first_name  VARCHAR2(50),
    last_name   VARCHAR2(50),
    email       VARCHAR2(128),
    manager     VARCHAR2(128),
    ssn         VARCHAR2(20),
    salary      NUMBER(10,2),
    phone       VARCHAR2(20)
);

GRANT SELECT, INSERT, UPDATE, DELETE ON hr.employees TO test;
