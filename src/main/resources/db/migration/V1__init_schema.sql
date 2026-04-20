-- Script de inicialização V1 para Oracle (Flyway)
-- Criação da sequência para Email_Outbox
CREATE SEQUENCE email_outbox_seq START WITH 1 INCREMENT BY 1;

-- 1. Criação da tabela Department
CREATE TABLE department (
    department_id VARCHAR2(100) NOT NULL PRIMARY KEY,
    name VARCHAR2(100) NOT NULL
);

-- 2. Criação da tabela Employee
CREATE TABLE employee (
    employee_id VARCHAR2(255) NOT NULL PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    email VARCHAR2(150) NOT NULL UNIQUE,
    gender VARCHAR2(20),
    department_department_id VARCHAR2(100) NOT NULL,
    CONSTRAINT employee_department_FK FOREIGN KEY (department_department_id) 
        REFERENCES department (department_id)
);

-- 3. Criação da tabela Diversity (diversity_report)
CREATE TABLE diversity_report (
    report_id VARCHAR2(255) NOT NULL PRIMARY KEY,
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
    CONSTRAINT diversity_department_FK FOREIGN KEY (department_department_id) 
        REFERENCES department (department_id)
);

-- 4. Criação da tabela Training
CREATE TABLE training (
    training_id VARCHAR2(100) NOT NULL PRIMARY KEY,
    title VARCHAR2(200) NOT NULL,
    description VARCHAR2(500),
    due_date DATE NOT NULL
);

-- 5. Criação da tabela Enrollment
CREATE TABLE enrollment (
    enrollment_id VARCHAR2(100) NOT NULL PRIMARY KEY,
    enrollment_date DATE NOT NULL,
    last_notification_date DATE,
    employee_employee_id VARCHAR2(255) NOT NULL,
    training_training_id VARCHAR2(100) NOT NULL,
    CONSTRAINT enrollment_employee_FK FOREIGN KEY (employee_employee_id) 
        REFERENCES employee (employee_id),
    CONSTRAINT enrollment_training_FK FOREIGN KEY (training_training_id) 
        REFERENCES training (training_id)
);

-- 6. Criação da tabela Completion
CREATE TABLE completion (
    completion_id VARCHAR2(100) NOT NULL PRIMARY KEY,
    completion_date DATE NOT NULL,
    result VARCHAR2(50) NOT NULL,
    enrollment_enrollment_id VARCHAR2(100),
    CONSTRAINT completion_enrollment_FK FOREIGN KEY (enrollment_enrollment_id) 
        REFERENCES enrollment (enrollment_id),
    CONSTRAINT completion_enrollment_UNQ UNIQUE (enrollment_enrollment_id)
);

-- Index definido na Entity completion
CREATE INDEX completion_IDX ON completion (enrollment_enrollment_id);

-- 7. Criação da tabela Email Outbox
CREATE TABLE email_outbox (
    id VARCHAR2(255) NOT NULL PRIMARY KEY,
    recipient VARCHAR2(150) NOT NULL,
    subject VARCHAR2(200) NOT NULL,
    body CLOB NOT NULL,
    status VARCHAR2(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    sent_at TIMESTAMP
);
