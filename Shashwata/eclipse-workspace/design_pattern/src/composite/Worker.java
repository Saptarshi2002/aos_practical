package composite;

public class Worker extends Employee {

    public Worker(String name, double salary) {
        super(name, salary);
    }

    @Override
    public void addEmployee(Employee employee) {
        throw new UnsupportedOperationException("Workers cannot have subordinates.");
    }

    @Override
    public void removeEmployee(Employee employee) {
        throw new UnsupportedOperationException("Workers cannot have subordinates.");
    }

    @Override
    public double calculateTotalSalary() {
        return this.salary;
    }

    @Override
    public int getTotalEmployees() {
        return 1; // A worker is an individual employee
    }
}
