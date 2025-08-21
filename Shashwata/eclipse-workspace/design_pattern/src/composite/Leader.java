package composite;

public class Leader extends Employee {

    public Leader(String name, double salary) {
        super(name, salary);
    }

    @Override
    public void addEmployee(Employee employee) {
        subordinates.add(employee);
    }

    @Override
    public void removeEmployee(Employee employee) {
        subordinates.remove(employee);
    }

    @Override
    public double calculateTotalSalary() {
        double totalSalary = this.salary;
        for (Employee subordinate : subordinates) {
            totalSalary += subordinate.calculateTotalSalary();
        }
        return totalSalary;
    }

    @Override
    public int getTotalEmployees() {
        int count = 1; // Count the leader itself
        for (Employee subordinate : subordinates) {
            count += subordinate.getTotalEmployees();
        }
        return count;
    }
}

