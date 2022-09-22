package com.example.retrofit_with_recyclerview.responses;

import com.squareup.moshi.Json;

public class EmployeeResponse {
    @Json(name = "id")
    private Long id;
    @Json(name = "name")
    private final String name;
    @Json(name = "department")
    private final String department;

    public EmployeeResponse(long id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public Long getId(){
        return this.id;
    }
}
