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

    public List<EmployeeResponse> getCrew() {
        return crew;
    }
}
