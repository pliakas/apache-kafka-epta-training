package edu.aegean.epta.kafka.producer.test.controller;

import edu.aegean.epta.kafka.producer.test.model.Employee;
import edu.aegean.epta.kafka.producer.test.service.EmployeeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @PostMapping
    public void publishEmployee(@RequestBody Employee employee) {
        employeeService.publishEmployeeData(employee);
    }

}