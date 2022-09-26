package com.example.retrofit_with_recyclerview.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.retrofit_with_recyclerview.responses.EmployeeResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Crew {
    private List<Employee> employeeList;

    public Crew(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<String> getAllDepartments(){
        Set<String> departmentSet = new HashSet();
        List<String> departmentList = new ArrayList<>();

        for(Employee e: this.employeeList){
            departmentSet.add(e.getDepartment());
        }

        departmentList.addAll(departmentSet);
        departmentList.sort(new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return str1.compareTo(str2);
            }
        });

        return departmentList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Employee> getEmployeesByDepartment(String department){
        Set<Employee> employeeSet = new HashSet<>();
        List<Employee> employeeList = new ArrayList<>();

        for(int i = 0; i < this.employeeList.size(); i++){
            Employee emp = this.employeeList.get(i);
            if (department.equalsIgnoreCase(emp.getDepartment())){
                employeeSet.add(emp);
            }
        }

        employeeList.addAll(employeeSet);
        employeeList.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee emp1, Employee emp2) {
                return emp1.compareTo(emp2);
            }
        }
        );

        return employeeList;
    }
}
