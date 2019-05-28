package com.mindex.challenge.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    private void getFullEmployee(Employee employee, ReportingStructure structure) {
    		// If we have direct reports, they need to be replaced with full versions
    		List<Employee> reports = employee.getDirectReports();
    		
    		if (reports == null)
    			return;
    		
    		List<Employee> newReports = new ArrayList<Employee>();
    		for (Employee report: reports) {
    			// Increment the number of reports in our returning structure
    			structure.setNumberOfReports(structure.getNumberOfReports() + 1);
    			
    			// Get full version of report, and full version of its reports as well
    			Employee newReport = employeeRepository.findByEmployeeId(report.getEmployeeId());
    			getFullEmployee(newReport, structure);
    			
    			newReports.add(newReport);
    		}
    		
    		// Replace old list of direct reports with new full version
    		employee.setDirectReports(newReports);
    }
    
    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Reading reporting structure of employee with id [{}]", id);
        
        ReportingStructure result = new ReportingStructure();
        
        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        
        result.setEmployee(employee);
        result.setNumberOfReports(0);
        getFullEmployee(employee, result);
        
        return result;
    }
}
