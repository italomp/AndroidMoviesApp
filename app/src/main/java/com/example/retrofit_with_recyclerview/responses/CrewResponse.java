package com.example.retrofit_with_recyclerview.responses;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CrewResponse {
    @Json(name = "crew")
    private final List<EmployeeResponse> crew;

    public CrewResponse(List<EmployeeResponse> crew) {
        this.crew = crew;
    }

    public List<String> getAllDepartments(){
        ArrayList result = new ArrayList();
        for(EmployeeResponse e: this.crew){
            if(!result.contains(e.getDepartment())){
                result.add(e.getDepartment());
            }
        }
        return result;
    }

    public String getToStringEmployeesByDepartment(String department, int maxLengthList){
        if(department == null) return null;
        String result = "";
        List<String> employeeList = new ArrayList<>();
        employeeList.addAll(getEmployeesByDepartment(department, maxLengthList));

        for(int i = 0; i < employeeList.size(); i++){
            int lastPosition = employeeList.size() - 1;
            if(i != lastPosition) {
                result += employeeList.get(i) + ", ";
                continue;
            }
            result += employeeList.get(i) + ".";
        }
        return result;
    }

    public Set<String> getEmployeesByDepartment(String department, int maxLengthList){
        HashSet<String> employeeSet = new HashSet<>();
        for(int i = 0; i < this.crew.size(); i++){
            if (employeeSet.size() == maxLengthList) break;

            EmployeeResponse emp = this.crew.get(i);
            if (department.equalsIgnoreCase(emp.getDepartment())){
                employeeSet.add(emp.getName());
            }
        }
        return employeeSet;
    }

}
