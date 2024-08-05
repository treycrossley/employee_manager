
INSERT INTO users (username, password, role) VALUES ('testuser', 'testpassword', 'USER');
INSERT INTO users (username, password, role) VALUES ('admin', 'adminpassword', 'USER');

-- Insert data for Revature
INSERT INTO company (company_name, location) VALUES ('Revature', 'Reston, VA');
 
 
-- Insert some sample data into the employee table
INSERT INTO employee (first_name, last_name, email, phone_number, job_id, salary, company_id, user_id)
VALUES 
    ('John', 'Doe', 'john.doe@example.com', '555-1234', 'IT_PROG', 60000, 1, 1),
    ('Jane', 'Smith', 'jane.smith@example.com', '555-5678','HR_REP', 50000, 1, 2),
    ('Bob', 'Johnson', 'bob.johnson@example.com', '555-8765','FIN_MGR', 70000, 1, 1);
 
