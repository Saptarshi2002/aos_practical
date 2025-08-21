package composite;


public class EmployeeManagementSystem {

    private CEO ceo;

    public EmployeeManagementSystem(CEO ceo) {
        this.ceo = ceo;
    }

    // Getter for the CEO
    public CEO getCeo() {
        return ceo;
    }

    // Add or remove an employee
    public void addEmployee(Employee employee, Employee supervisor) {
        supervisor.addEmployee(employee);
    }

    public void removeEmployee(Employee employee, Employee supervisor) {
        supervisor.removeEmployee(employee);
    }

    // Promote an employee
    public void promoteEmployee(Employee employee, Employee newSupervisor) {
        // Find current supervisor and remove employee
        Employee currentSupervisor = findSupervisor(employee);
        if (currentSupervisor != null) {
            currentSupervisor.removeEmployee(employee);
        }
        // Add employee to the new supervisor
        newSupervisor.addEmployee(employee);
    }

    // Calculate total salary of the company or employee
    public double calculateTotalSalary(Employee employee) {
        return employee.calculateTotalSalary();
    }

    // Get total number of employees
    public int getTotalEmployees(Employee employee) {
        return employee.getTotalEmployees();
    }

    // Helper method to find supervisor
    private Employee findSupervisor(Employee employee) {
        return findSupervisorRecursively(ceo, employee);
    }

    private Employee findSupervisorRecursively(Employee supervisor, Employee employee) {
        for (Employee sub : supervisor.getSubordinates()) {
            if (sub.equals(employee)) {
                return supervisor;
            }
            Employee result = findSupervisorRecursively(sub, employee);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}


