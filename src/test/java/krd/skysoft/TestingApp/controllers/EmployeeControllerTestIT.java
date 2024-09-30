package krd.skysoft.TestingApp.controllers;

import krd.skysoft.TestingApp.dto.EmployeeDto;
import krd.skysoft.TestingApp.entities.Employee;
import krd.skysoft.TestingApp.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EmployeeControllerTestIT extends AbstractIntegrationTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();

    }

    @Test
    void testEmployeeById_success(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.get()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
//                .expectBody(EmployeeDto.class)
                .expectBody()
//                .isEqualTo(testEmployeeDto);
//                .value((employeeDto)->{
//                    assertThat(employeeDto.getId()).isEqualTo(testEmployee.getId());
//                    assertThat(employeeDto.getName()).isEqualTo(testEmployee.getName());
//                    assertThat(employeeDto.getEmail()).isEqualTo(testEmployee.getEmail());
//                    assertThat(employeeDto.getSalary()).isEqualTo(testEmployee.getSalary());
//
//                });
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.name").isEqualTo(savedEmployee.getName())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail())
                .jsonPath("$.salary").isEqualTo(savedEmployee.getSalary());

    }

    @Test
    void testEmployeeById_failure(){
//        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.get()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void testCreateEmployee_whenEmployeeAlreadyExists_thenThrowsException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);

        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();

    }

    @Test
    void testCreateNewEmployee_whenEmployeeDoesNotExist_success(){
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.email").isEqualTo(testEmployee.getEmail())
                .jsonPath("$.salary").isEqualTo(testEmployee.getSalary());
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExist_thenThrowsException(){
        webTestClient.put()
                .uri("/employees/999")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateEmployee_whenAttemptingToUpdateEmployee_thenThrowsException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setEmail("rando@gmail.com");
        testEmployeeDto.setName("random user");
        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testUpdateEmployee_whenEmployeeIsValid_success(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setName("random user");

        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testEmployeeDto);
    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExist_thenThrowsException(){
        webTestClient.delete()
                .uri("/employees/999")
                .exchange()
                .expectStatus().isNotFound();
    }
    @Test
    void testDeleteEmployee_whenEmployeeIsValid_success(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.delete()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);
//
//        webTestClient.delete()
//                .uri("/employees/{id}", savedEmployee.getId())
//                .exchange()
//                .expectStatus().isNotFound();

        // just to make sure that secondtime deleted
    }



}