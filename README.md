# Employee Management Application

## Description
I developed a simple API using Spring Boot to create and manage employees. The goal was to build an application that allows for storing and tracking employee data effectively.

## Features
Technology Stack: Built with Java 17 and Spring Boot 3
Database: Utilized an in-memory H2 database for ease of development; however, the application is also compatible with PostgreSQL for production-level persistence.
API Interactions: All interactions are handled via HTTP requests. Tools like Postman were used for testing and verifying API endpoints.
## Implemented User Stories

### Employee Management
- Create a New Employee: Users can create new employee records.
- View All Employees: Users can retrieve a list of all employees.
- View Employee by ID: Users can view a specific employee by their ID.
- Update Employee Details: Users can update the details of an existing employee.
- Delete Employee by ID: Users can delete an employee by their ID.
### User Account Management:
- Create Account: Users can create an account to manage their employees.
- Login: Users can log in to their account.
- View Own Employees: Users can view employees associated with their account.
### Roles and Permissions:
- Admin Role: Admins can view all employees across accounts.
- User Role: Regular users can only view employees associated with their own account.
## Optional Enhancements
Role-based Access Control: Implemented roles such as USER and ADMIN to differentiate access levels.
Testing: Incorporated JUnit for service layer testing with comprehensive coverage.
## API Response Handling
Status Codes and Responses: Properly handled HTTP status codes and JSON response bodies. For example, a request to /employees/5 will return a 404 status code if the resource is not found, or a 200 status code if it is available.
## Presentation
Showcase: Used Postman to demonstrate how the application manages HTTP requests and responses.
Documentation: Provided clear, concise communication and professional presentation of the project.
## Key Considerations
Project Due Date: Completed and finalized on August 6th, 2024.
Code Management: Regularly updated and maintained the codebase to ensure stability and functionality.
