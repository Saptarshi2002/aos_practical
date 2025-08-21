package composite;

import java.util.ArrayList;
import java.util.List;

public abstract class Employee {
    protected String name;
    protected double salary;
    protected List<Employee> subordinates = new ArrayList<>();

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public abstract void addEmployee(Employee employee);
    public abstract void removeEmployee(Employee employee);
    public abstract double calculateTotalSalary();
    public abstract int getTotalEmployees();

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }
}

