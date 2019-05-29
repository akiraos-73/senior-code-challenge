package com.mindex.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

@Component
public class DataBootstrap {
    private static final String DATASTORE_LOCATION = "/static/employee_database.json";
    
    private static final String COMP_DATASTORE_LOCATION = "/static/compensation_database.json";

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        InputStream inputStream = this.getClass().getResourceAsStream(DATASTORE_LOCATION);

        Employee[] employees = null;

        try {
            employees = objectMapper.readValue(inputStream, Employee[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Employee employee : employees) {
            employeeRepository.insert(employee);
        }
        
        InputStream compInputStream = this.getClass().getResourceAsStream(COMP_DATASTORE_LOCATION);
        
        Compensation[] compensations = null;
        
        try {
        		compensations = objectMapper.readValue(compInputStream, Compensation[].class);
        }
        catch (IOException e) {
        		throw new RuntimeException(e);
        }
        
        for (Compensation compensation : compensations) {
        		compensationRepository.insert(compensation);
        }
    }
}
