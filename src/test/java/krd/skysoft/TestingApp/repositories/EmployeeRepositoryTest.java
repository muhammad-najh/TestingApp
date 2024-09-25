package krd.skysoft.TestingApp.repositories;

import krd.skysoft.TestingApp.TestContainerConfiguration;
import krd.skysoft.TestingApp.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


//@SpringBootTest //this will load all classes
@Import(TestContainerConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //this will ignore h2 database and use only docker image
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .id(1L)
                .name("hama")
                .email("hama@gmail.com")
                .salary(1000L)
                .build();
    }


    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee() {
        // Arrange,Given
        employeeRepository.save(employee);
        //When,Act
        List<Employee> employeesList = employeeRepository.findByEmail(employee.getEmail());
        // Assert, then
       assertThat(employeesList).isNotNull();
       assertThat(employeesList).isNotEmpty();
       assertThat(employeesList.get(0).getEmail()).isEqualTo(employee.getEmail());
    }
    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyEmployeeList() {
        //given
        String email = "notfound@gmail.com";
        //when
        List<Employee> employeesList = employeeRepository.findByEmail(email);
        //
        assertThat(employeesList).isNotNull();
        assertThat(employeesList).isNotEmpty();

    }
}