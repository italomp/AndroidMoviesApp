package com.example.retrofit_with_recyclerview.util;

import com.example.retrofit_with_recyclerview.models.Crew;
import com.example.retrofit_with_recyclerview.models.Employee;
import com.example.retrofit_with_recyclerview.responses.CrewResponse;
import com.example.retrofit_with_recyclerview.responses.EmployeeResponse;

import java.util.ArrayList;
import java.util.List;

public class CrewMapper {

    public static Crew mapperCrewResponseToCrew(CrewResponse crewResponse){
        List<Employee> employeeList = mapperEmployeeResponseListToEmployeeList(crewResponse.getCrew());
        return new Crew(employeeList);
    }

    public static List<Employee> mapperEmployeeResponseListToEmployeeList(List<EmployeeResponse> empRespList){
        List<Employee> result = new ArrayList<>();

        for(EmployeeResponse empResp: empRespList){
            Employee newEmployee = new Employee(empResp.getId(), empResp.getName(), empResp.getDepartment());
            result.add(newEmployee);
        }

        return result;
    }
}
