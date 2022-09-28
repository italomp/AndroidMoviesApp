package com.example.retrofit_with_recyclerview.models;

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
}
