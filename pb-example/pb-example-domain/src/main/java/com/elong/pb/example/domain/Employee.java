package com.elong.pb.example.domain;

/**
 * project：pb-example
 * Date: 15/4/24
 * Time: 17:12
 * file: Employee.java
 *
 * @author dewei.tian@corp.elong.com
 */
public class Employee {

    private String name;

    private Double salary;

    private String email;

    private String address;

    public Employee(String name, Double salary, String email, String address) {
        this.name = name;
        this.salary = salary;
        this.email = email;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
