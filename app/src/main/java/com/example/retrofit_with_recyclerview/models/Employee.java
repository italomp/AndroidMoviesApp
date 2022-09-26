package com.example.retrofit_with_recyclerview.models;

public class Employee implements Comparable{
    private long id;
    private String name;
    private String department;

    public Employee(long id, String name, String department) {
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

    public long getId() {
        return id;
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
