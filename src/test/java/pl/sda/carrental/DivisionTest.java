package pl.sda.carrental;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import pl.sda.carrental.controller.DivisionController;
import pl.sda.carrental.model.dataTransfer.CreateDivisionDTO;
import pl.sda.carrental.model.dataTransfer.EmployeeDTO;
import pl.sda.carrental.model.entity.Division;
import pl.sda.carrental.model.entity.userEntities.Employee;
import pl.sda.carrental.model.repository.DivisionRepository;
import pl.sda.carrental.model.repository.userRepositories.EmployeeRepository;
import pl.sda.carrental.service.DivisionService;
import pl.sda.carrental.service.EmployeeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DivisionTest {
    @MockBean
    DivisionRepository divisionRepository;
    @MockBean
    EmployeeRepository employeeRepository;
    Employee testEmployee;
    Employee testEmployee2;
    Employee testEmployee3;
    Division testDivision;
    @MockBean
    EmployeeService employeeService;
    @Autowired
    DivisionService divisionService;
    @Autowired
    MockMvc mockMvc;
    @InjectMocks
    DivisionController divisionController;

    @BeforeEach
    void createTestEmployee() {
        this.testEmployee = Employee.builder().id(1L).name("Andrzej Pracuś").email("andrzej.pracus@gmail.com").build();
        this.testEmployee2 = Employee.builder().id(2L).name("Michał Robotnik").email("michal.robotnik@gmail.com").build();
        this.testEmployee3 = Employee.builder().id(3L).name("Michał Robotnik").email("michal.robotnik@gmail.com").build();
        List<Employee> employees = new ArrayList<>();
        employees.add(testEmployee);

        this.testDivision = Division.builder().employees(employees).division_id(1L).build();
    }

    @Test
    void shouldAddEmployee() {
        List<Division> mockDivisions = new ArrayList<>();
        mockDivisions.add(testDivision);
        Mockito.when(divisionRepository.findAll()).thenReturn(mockDivisions);

        List<Division> divisions = divisionRepository.findAll();
        Division mockDivision = divisions.get(0);
        divisionService.addEmployees(mockDivision, testEmployee2);
        assertTrue(mockDivision.getEmployees().contains(testEmployee2));
    }

    @Test
    void shouldAddEmployees() {
        List<Employee> employees = new ArrayList<>(Arrays.asList(testEmployee2, testEmployee3));
        employees.forEach(e -> {
            Assertions.assertFalse(testDivision.getEmployees().contains(e));
        });
        divisionService.addEmployees(testDivision, employees);
        employees.forEach(e -> {
            assertTrue(testDivision.getEmployees().contains(e));
        });
    }

    @Test
    void testDivisionHelloWorld() throws Exception {
        mockMvc.perform(post("/divisions/hello-world"))
                .andExpect(status().isOk())
                .andExpect(view().name(not("homes")))
                .andExpect(view().name("home"));
    }
    @Test
    void shouldThrowException() throws Exception {
        Mockito.when(employeeService.canBecomeManager(1L)).thenReturn(false);
        EmployeeDTO employeeDTO = EmployeeDTO.builder().id(1L).build();
        ObjectMapper objectMapper = new ObjectMapper();

        CreateDivisionDTO newDivision =  CreateDivisionDTO.builder()
                .division_id(testDivision.getDivision_id())
                .manager(employeeDTO)
                .build();

        mockMvc.perform(
                post("/divisions/new/json").contentType("application/json").content(objectMapper.writeValueAsString(newDivision)))
                .andExpect(result -> assertInstanceOf(IllegalArgumentException.class, result.getResolvedException()));
    }
}
