package krd.skysoft.TestingApp.controllers;

import krd.skysoft.TestingApp.TestContainerConfiguration;
import krd.skysoft.TestingApp.dto.EmployeeDto;
import krd.skysoft.TestingApp.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTest {

    @Autowired
    public WebTestClient webTestClient;

    Employee testEmployee=Employee.builder()
            .id(1L)
                .name("muhamad")
                .email("muhamad@gmail.com")
                .salary(100L)
                .build();
    EmployeeDto testEmployeeDto=EmployeeDto.builder()
            .id(1L)
                .name("muhamad")
                .email("muhamad@gmail.com")
                .salary(100L)
                .build();

}
