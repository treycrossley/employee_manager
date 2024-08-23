package com.example.item_manager.service;

import com.example.item_manager.model.Company;
import com.example.item_manager.model.Employee;
import com.example.item_manager.model.User;
import com.example.item_manager.repository.CompanyRepository;
import com.example.item_manager.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee testEmployee;
    private Company testCompany;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test objects
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testCompany = new Company();
        testCompany.setCompanyId(1L);
        testCompany.setName("Revature");

        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setEmail("john.doe@example.com");
        testEmployee.setPhoneNumber("1234567890");
        testEmployee.setJobId("DEV");
        testEmployee.setSalary(60000);
        testEmployee.setCompanyId(testCompany.getCompanyId());
    }

    @Test
    void testCreateEmployee_Success() {
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(testCompany));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        Employee createdEmployee = employeeService.createEmployee(testEmployee);

        assertNotNull(createdEmployee);
        assertEquals(testEmployee.getFirstName(), createdEmployee.getFirstName());
        assertEquals(testEmployee.getLastName(), createdEmployee.getLastName());
        assertEquals(testCompany, createdEmployee.getCompany());
        verify(employeeRepository).save(testEmployee);
    }

    @Test
    void testCreateEmployee_CompanyNotFound() {
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeService.createEmployee(testEmployee);
        });

        assertEquals("Company not found", exception.getMessage());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testCreateEmployee_CompanyIdNotProvided() {
        testEmployee.setCompanyId(null);
        when(userService.getCurrentUser()).thenReturn(testUser);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeService.createEmployee(testEmployee);
        });

        assertEquals("Company ID must be provided.", exception.getMessage());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(testEmployee));

        List<Employee> employees = employeeService.getAllEmployees();

        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals(testEmployee.getFirstName(), employees.get(0).getFirstName());
        verify(employeeRepository).findAll();
    }

    @Test
    void testGetEmployeeById() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(testEmployee));

        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1L);

        assertTrue(foundEmployee.isPresent());
        assertEquals(testEmployee.getFirstName(), foundEmployee.get().getFirstName());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Employee> foundEmployee = employeeService.getEmployeeById(1L);

        assertFalse(foundEmployee.isPresent());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testUpdateEmployee() {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setFirstName("Jane");
        updatedEmployee.setLastName("Doe");
        updatedEmployee.setEmail("jane.doe@example.com");
        updatedEmployee.setPhoneNumber("0987654321");
        updatedEmployee.setJobId("SENIOR_DEV");
        updatedEmployee.setSalary(70000);
        updatedEmployee.setCompanyId(testCompany.getCompanyId());

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        Employee result = employeeService.updateEmployee(1L, updatedEmployee);

        assertNotNull(result);
        assertEquals(updatedEmployee.getFirstName(), result.getFirstName());
        assertEquals(updatedEmployee.getLastName(), result.getLastName());
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(testEmployee);
    }

    @Test
    void testUpdateEmployee_NotFound() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeService.updateEmployee(1L, testEmployee);
        });

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testDeleteEmployee() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(testEmployee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).delete(testEmployee);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeService.deleteEmployee(1L);
        });

        assertEquals("Employee not found", exception.getMessage());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testGetEmployeesByUser() {
        when(userService.getUserById(anyLong())).thenReturn(testUser);
        when(employeeRepository.findByUser(any(User.class))).thenReturn(Arrays.asList(testEmployee));

        List<Employee> employees = employeeService.getEmployeesByUser(1L);

        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals(testEmployee.getFirstName(), employees.get(0).getFirstName());
        verify(userService).getUserById(1L);
        verify(employeeRepository).findByUser(testUser);
    }
}
