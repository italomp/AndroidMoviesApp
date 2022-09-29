package com.example.retrofit_with_recyclerview.models;

import java.util.Objects;

public class Employee implements Comparable{
    private long id;
    private String name;
    private String department;
    private String posterPath;

    public Employee(long id, String name, String department, String posterPath) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.posterPath = posterPath;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public long getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public int compareTo(Object o) {
        Employee anotherEmp = (Employee) o;

        if(name.compareTo(anotherEmp.getName()) < 0)
            return -1;
        else if(name.compareTo(anotherEmp.getName()) > 0)
            return 1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return getId() == employee.getId() && getName().equals(employee.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
