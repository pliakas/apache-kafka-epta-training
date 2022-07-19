package edu.aegean.epta.kafka.producer.test.service;

import edu.aegean.epta.kafka.producer.test.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private KafkaTemplate<Integer, Employee> kafkaTemplate;

    public void publishEmployeeData(Employee employee) {
        kafkaTemplate.send("producer-test-topic",employee);
    }


}