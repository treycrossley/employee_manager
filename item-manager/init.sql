-- Create the company table
CREATE TABLE company (
    company_id SERIAL PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    location VARCHAR(100)
);
 
-- Insert data for Revature
INSERT INTO company (company_name, location)
VALUES 
    ('Revature', 'Reston, VA');
 
-- Create the employee table
CREATE TABLE employee (
    employee_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    hire_date DATE NOT NULL,  -- Add hire_date column
    job_id VARCHAR(10) NOT NULL,
    salary NUMERIC(10, 2) NOT NULL,
    company_id INT,
    CONSTRAINT fk_company
        FOREIGN KEY(company_id) 
        REFERENCES company(company_id)
);
 
-- Insert some sample data into the employee table
INSERT INTO employee (first_name, last_name, email, phone_number, hire_date, job_id, salary, company_id)
VALUES 
    ('John', 'Doe', 'john.doe@example.com', '555-1234', '2024-01-01', 'IT_PROG', 60000, 1),
    ('Jane', 'Smith', 'jane.smith@example.com', '555-5678', '2024-01-02', 'HR_REP', 50000, 1),
    ('Bob', 'Johnson', 'bob.johnson@example.com', '555-8765', '2024-01-03', 'FIN_MGR', 70000, 1);
 
-- Query to select all employees
SELECT * FROM employee;
