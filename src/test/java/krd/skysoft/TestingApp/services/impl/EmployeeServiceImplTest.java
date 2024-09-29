package krd.skysoft.TestingApp.services.impl;

import krd.skysoft.TestingApp.TestContainerConfiguration;
import krd.skysoft.TestingApp.dto.EmployeeDto;
import krd.skysoft.TestingApp.entities.Employee;
import krd.skysoft.TestingApp.exceptions.ResourceNotFoundException;
import krd.skysoft.TestingApp.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

//@SpringBootTest //this will load all classes
@Import(TestContainerConfiguration.class)
//@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //this will ignore h2 database and use only docker image
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Spy
    private ModelMapper modelMapper ;


    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;

    @BeforeEach
    void setUp() {
        mockEmployee=Employee.builder()
                .id(1L).name("hama").email("hama@gmail.com").salary(1000L).build();
        mockEmployeeDto=modelMapper.map(mockEmployee, EmployeeDto.class);
    }

    @Test
    void testEmployeeById_WhenEmployeeIdIsPresent_ThenEmployeeIsReturned() {
        Long id=mockEmployee.getId();
        Employee mockEmployee=Employee.builder()

        .id(id).name("hama").email("hama@gmail.com").salary(1000L).build();
        //assign
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee)); //stubbing
        //act
        EmployeeDto employeeDto=employeeService.getEmployeeById(id);
        //assert
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        assertThat(employeeDto).isNotNull();

       // verify(employeeRepository).save(null); failed test case//verify that findbyid func called in moc(employeerepository)
        //verify(employeeRepository).findById(id); // passed test case//verify that findbyid func called in moc(employeerepository)
        //verify(employeeRepository,times(1)).findById(id);
//        verify(employeeRepository,times(1)).findById(id);
//        verify(employeeRepository,times(2)).findById(id);
//        verify(employeeRepository,atLeast(2)).findById(id);
//        verify(employeeRepository,atLeast(1)).findById(id);
//        verify(employeeRepository,atMost(2)).findById(id);
//        verify(employeeRepository,atMost(1)).findById(id);
        verify(employeeRepository,only()).findById(1L);

    }

    @Test
    void testEmployeeById_WhenEmployeeIdIsNotPresent_ThenThrowEmployeeNotFoundException() {
        //assign
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act//assert
        assertThatThrownBy(()->employeeService.getEmployeeById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository,times(1)).findById(1L);
    }

    @Test
    void testCreateEmployee_WhenValidEmployee_ThenCreateNewEmployee() {
        Long id=mockEmployee.getId();
        //assign
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        //act
        EmployeeDto employeeDto=employeeService.createNewEmployee(mockEmployeeDto);
        //assert
       assertThat(employeeDto).isNotNull();
       assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());

        ArgumentCaptor<Employee>employeeArgumentCaptor=ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());
        Employee captorEmployee=employeeArgumentCaptor.getValue();
//      System.out.println(captorEmployee.getEmail());
        assertThat(captorEmployee.getEmail()).isEqualTo(mockEmployeeDto.getEmail());

    }

    @Test
    void testCreateEmployee_WhenEmployeeEmailIsAlreadyExist_ThenThrowException() {
        //arrange
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));
        //act // assert
        assertThatThrownBy(()->employeeService.createNewEmployee(mockEmployeeDto))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Employee already exists with email: "+mockEmployeeDto.getEmail());

        verify(employeeRepository,times(1)).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository,never()).save(any(Employee.class));
    }
    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowEmployeeNotFoundException() {
        //arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        //act,assert
        assertThatThrownBy(()->employeeService.updateEmployee(1L,mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");
        verify(employeeRepository,times(1)).findById(1L);
        verify(employeeRepository,never()).save(any(Employee.class));

    }

    @Test
    void testUpdateEmployee_whenAttemptingToUpdateEmployee_thenThrowException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));

       mockEmployee.setId(2L);
       mockEmployee.setEmail("random@gmail.com");

        assertThatThrownBy(()->employeeService.updateEmployee(1L,mockEmployeeDto))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository,times(1)).findById(1L);
        verify(employeeRepository,never()).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee_whenEmployee_thenUpdateEmployee() {
        //arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("randomName");
        mockEmployeeDto.setSalary(120L);

        Employee newEmployee=modelMapper.map(mockEmployeeDto, Employee.class);
        when(employeeRepository.save(mockEmployee)).thenReturn(newEmployee);
        //act
        EmployeeDto updateEmployeeDto=employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto);
        assertThat(updateEmployeeDto).isEqualTo(mockEmployeeDto);

        verify(employeeRepository,times(1)).findById(1L);
        verify(employeeRepository,times(1)).save(any(Employee.class));

    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExist_thenThrowEmployeeNotFoundException() {
        when(employeeRepository.existsById(1L)).thenReturn(false);
        assertThatThrownBy(()->employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository,times(1)).existsById(1L);
        verify(employeeRepository,never()).deleteById(any());
    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesExist_thenDeleteEmployee() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        assertThatCode(()->employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();

        verify(employeeRepository,times(1)).deleteById(any());




    }



}