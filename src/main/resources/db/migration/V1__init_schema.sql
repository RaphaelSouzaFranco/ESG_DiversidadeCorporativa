-- Script de inicialização V1 para Oracle (Flyway)

-- 0. Sequence
CREATE SEQUENCE email_outbox_seq START WITH 1 INCREMENT BY 1;

-- 1. Tabela Department
CREATE TABLE department (
    department_id VARCHAR2(100) NOT NULL,
    name VARCHAR2(100) NOT NULL,
    CONSTRAINT department_pk PRIMARY KEY (department_id)
);

-- 2. Tabela Employee
CREATE TABLE employee (
    employee_id VARCHAR2(255) NOT NULL,
    name VARCHAR2(100) NOT NULL,
    email VARCHAR2(150) NOT NULL,
    gender VARCHAR2(20),
    department_department_id VARCHAR2(100) NOT NULL,
    CONSTRAINT employee_pk PRIMARY KEY (employee_id),
    CONSTRAINT employee_email_uk UNIQUE (email),
    CONSTRAINT employee_department_fk FOREIGN KEY (department_department_id) 
        REFERENCES department (department_id)
);

-- 3. Tabela Diversity Report
CREATE TABLE diversity_report (
    report_id VARCHAR2(255) NOT NULL,
    total_employees NUMBER(19, 0) DEFAULT 0 NOT NULL,
    total_male NUMBER(19, 0) DEFAULT 0 NOT NULL,
    total_female NUMBER(19, 0) DEFAULT 0 NOT NULL,
    total_other NUMBER(19, 0) DEFAULT 0 NOT NULL,
    total_not_informed NUMBER(10, 0) DEFAULT 0 NOT NULL,
    percentage_male NUMBER,
    percentage_female NUMBER,
    percentage_other NUMBER,
    created_at TIMESTAMP NOT NULL,
    department_department_id VARCHAR2(100) NOT NULL,
    CONSTRAINT diversity_pk PRIMARY KEY (report_id),
    CONSTRAINT diversity_department_fk FOREIGN KEY (department_department_id) 
        REFERENCES department (department_id)
);

-- 4. Tabela Training
CREATE TABLE training (
    training_id VARCHAR2(100) NOT NULL,
    title VARCHAR2(200) NOT NULL,
    description VARCHAR2(500),
    due_date DATE NOT NULL,
    CONSTRAINT training_pk PRIMARY KEY (training_id)
);

-- 5. Tabela Enrollment
CREATE TABLE enrollment (
    enrollment_id VARCHAR2(100) NOT NULL,
    enrollment_date DATE NOT NULL,
    last_notification_date DATE,
    employee_employee_id VARCHAR2(255) NOT NULL,
    training_training_id VARCHAR2(100) NOT NULL,
    CONSTRAINT enrollment_pk PRIMARY KEY (enrollment_id),
    CONSTRAINT enrollment_employee_fk FOREIGN KEY (employee_employee_id) 
        REFERENCES employee (employee_id),
    CONSTRAINT enrollment_training_fk FOREIGN KEY (training_training_id) 
        REFERENCES training (training_id)
);

-- 6. Tabela Completion
CREATE TABLE completion (
    completion_id VARCHAR2(100) NOT NULL,
    completion_date DATE NOT NULL,
    result VARCHAR2(50) NOT NULL,
    enrollment_enrollment_id VARCHAR2(100),
    CONSTRAINT completion_pk PRIMARY KEY (completion_id),
    CONSTRAINT completion_enrollment_fk FOREIGN KEY (enrollment_enrollment_id) 
        REFERENCES enrollment (enrollment_id),
    CONSTRAINT completion_enrollment_uk UNIQUE (enrollment_enrollment_id)
);

-- ⚠️ REMOVIDO: índice duplicado (já existe por causa do UNIQUE)

-- 7. Tabela Email Outbox
CREATE TABLE email_outbox (
    id VARCHAR2(255) NOT NULL,
    recipient VARCHAR2(150) NOT NULL,
    subject VARCHAR2(200) NOT NULL,
    body CLOB NOT NULL,
    status VARCHAR2(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    sent_at TIMESTAMP,
    CONSTRAINT email_outbox_pk PRIMARY KEY (id)
);