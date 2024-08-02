package com.example.item_manager.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", updatable = false)
    private Long employee_id;

    @Column(name = "first_name", nullable = false)
    private String first_name;
    @Column(name = "last_name", nullable = false)
    private String last_name;

    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "phone_number", nullable = false)
    private String phone_number;

    @Column(name = "job_id", nullable = false)
    private String job;
    @Column(nullable = false, columnDefinition = "numeric(10,2)")
    private double salary;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /*
    CREATE TABLE employee (
       employee_id SERIAL PRIMARY KEY,
       first_name VARCHAR(50) NOT NULL,
       last_name VARCHAR(50) NOT NULL,
       email VARCHAR(100) NOT NULL UNIQUE,
       phone_number VARCHAR(20),
       job_id VARCHAR(10) NOT NULL,
       salary NUMERIC(10, 2) NOT NULL,
       company_id INT,
       CONSTRAINT fk_company
           FOREIGN KEY(company_id)
           REFERENCES company(company_id)
    );*/
}
