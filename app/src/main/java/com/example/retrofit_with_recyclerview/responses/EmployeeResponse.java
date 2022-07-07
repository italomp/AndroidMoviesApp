package com.example.retrofit_with_recyclerview.responses;

public class EmployeeResponse {
    private final String name;
    private final String department;

    public EmployeeResponse(String name, String department) {
        this.name = name;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }
}
