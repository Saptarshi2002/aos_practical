package composite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeManagementUI {
    private static EmployeeManagementSystem managementSystem;
    private static JTextArea displayArea;
    private static JTextField nameField, salaryField, supervisorField;
    private static JComboBox<String> employeeTypeComboBox;
    private static JComboBox<String> promoteEmployeeComboBox;

    public static void main(String[] args) {
        // Initialize a sample CEO and Employee Management System
        CEO ceo = new CEO("John CEO", 100000);
        managementSystem = new EmployeeManagementSystem(ceo);

        // Create initial employees
        Manager manager1 = new Manager("Manager 1", 50000);
        Leader leader1 = new Leader("Leader 1", 30000);
        Worker worker1 = new Worker("Worker 1", 15000);

        managementSystem.addEmployee(manager1, ceo);
        managementSystem.addEmployee(leader1, manager1);
        managementSystem.addEmployee(worker1, leader1);

        // Create the Swing UI
        JFrame frame = new JFrame("Employee Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        // Display area for showing current structure and results
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel for user inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));

        // Fields for adding/removing employees
        inputPanel.add(new JLabel("Employee Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Employee Salary:"));
        salaryField = new JTextField();
        inputPanel.add(salaryField);

        inputPanel.add(new JLabel("Employee Type:"));
        employeeTypeComboBox = new JComboBox<>(new String[] { "Manager", "Leader", "Worker" });
        inputPanel.add(employeeTypeComboBox);

        inputPanel.add(new JLabel("Supervisor Name:"));
        supervisorField = new JTextField();
        inputPanel.add(supervisorField);

        // Add the panel to the frame
        frame.add(inputPanel, BorderLayout.NORTH);

        // Buttons for adding, removing, promoting employees
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Add employee button
        JButton addButton = new JButton("Add Employee");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });
        buttonPanel.add(addButton);

        // Remove employee button
        JButton removeButton = new JButton("Remove Employee");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEmployee();
            }
        });
        buttonPanel.add(removeButton);

        // Promote employee button
        JButton promoteButton = new JButton("Promote Employee");
        promoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promoteEmployee();
            }
        });
        buttonPanel.add(promoteButton);

        // Calculate total salary button
        JButton totalSalaryButton = new JButton("Total Salary");
        totalSalaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTotalSalary();
            }
        });
        buttonPanel.add(totalSalaryButton);

        // Total employees button
        JButton totalEmployeesButton = new JButton("Total Employees");
        totalEmployeesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTotalEmployees();
            }
        });
        buttonPanel.add(totalEmployeesButton);

        // Add buttons to the frame
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // ComboBox for promoting employee
        promoteEmployeeComboBox = new JComboBox<>();
        updatePromoteEmployeeComboBox();
        frame.add(promoteEmployeeComboBox, BorderLayout.EAST);

        // Display the initial company structure
        updateDisplayArea();

        // Show the frame
        frame.setVisible(true);
    }

    // Update display area to show current hierarchy and results
    private static void updateDisplayArea() {
        displayArea.setText("");
        displayArea.append("Company Structure:\n");
        displayArea.append("CEO: " + managementSystem.getTotalEmployees(managementSystem.getCeo()) + " employees, ");
        displayArea.append("Total Salary: " + managementSystem.calculateTotalSalary(managementSystem.getCeo()) + "\n");

        displayArea.append("\nCurrent Employees:\n");
        listEmployees(managementSystem.getCeo(), "CEO");

        displayArea.append("\n");
    }

    // Recursively list employees in the structure
    private static void listEmployees(Employee employee, String level) {
        displayArea.append(level + " - " + employee.getName() + ", Salary: " + employee.getSalary() + "\n");
        for (Employee subordinate : employee.getSubordinates()) {
            listEmployees(subordinate, "  " + level);
        }
    }

    // Add an employee
    private static void addEmployee() {
        String name = nameField.getText();
        double salary;
        try {
            salary = Double.parseDouble(salaryField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid salary!");
            return;
        }
        String type = (String) employeeTypeComboBox.getSelectedItem();
        String supervisorName = supervisorField.getText();

        Employee supervisor = findEmployeeByName(supervisorName);
        if (supervisor == null) {
            JOptionPane.showMessageDialog(null, "Supervisor not found!");
            return;
        }

        Employee newEmployee = null;
        switch (type) {
            case "Manager":
                newEmployee = new Manager(name, salary);
                break;
            case "Leader":
                newEmployee = new Leader(name, salary);
                break;
            case "Worker":
                newEmployee = new Worker(name, salary);
                break;
        }

        if (newEmployee != null) {
            managementSystem.addEmployee(newEmployee, supervisor);
            updateDisplayArea();
        }
    }

    // Remove an employee
    private static void removeEmployee() {
        String name = nameField.getText();
        Employee employee = findEmployeeByName(name);
        if (employee != null) {
            Employee supervisor = findSupervisor(employee);
            if (supervisor != null) {
                managementSystem.removeEmployee(employee, supervisor);
                updateDisplayArea();
            } else {
                JOptionPane.showMessageDialog(null, "Cannot remove the CEO!");
            }
        }
    }

    // Promote an employee
    private static void promoteEmployee() {
        Employee employee = findEmployeeByName((String) promoteEmployeeComboBox.getSelectedItem());
        if (employee != null) {
            String newSupervisorName = supervisorField.getText();
            Employee newSupervisor = findEmployeeByName(newSupervisorName);
            if (newSupervisor != null) {
                managementSystem.promoteEmployee(employee, newSupervisor);
                updateDisplayArea();
            }
        }
    }

    // Display total salary of company
    private static void displayTotalSalary() {
        double totalSalary = managementSystem.calculateTotalSalary(managementSystem.getCeo());
        JOptionPane.showMessageDialog(null, "Total Salary of Company: " + totalSalary);
    }

    // Display total number of employees in company
    private static void displayTotalEmployees() {
        int totalEmployees = managementSystem.getTotalEmployees(managementSystem.getCeo());
        JOptionPane.showMessageDialog(null, "Total Employees in Company: " + totalEmployees);
    }

    // Find employee by name
    private static Employee findEmployeeByName(String name) {
        return findEmployeeRecursively(managementSystem.getCeo(), name);
    }

    private static Employee findEmployeeRecursively(Employee supervisor, String name) {
        if (supervisor.getName().equals(name)) {
            return supervisor;
        }
        for (Employee subordinate : supervisor.getSubordinates()) {
            Employee result = findEmployeeRecursively(subordinate, name);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    // Find supervisor of an employee
    private static Employee findSupervisor(Employee employee) {
        return findSupervisorRecursively(managementSystem.getCeo(), employee);
    }

    private static Employee findSupervisorRecursively(Employee supervisor, Employee employee) {
        for (Employee subordinate : supervisor.getSubordinates()) {
            if (subordinate.equals(employee)) {
                return supervisor;
            }
            Employee result = findSupervisorRecursively(subordinate, employee);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    // Update promote employee combo box
    private static void updatePromoteEmployeeComboBox() {
        promoteEmployeeComboBox.removeAllItems();
        promoteEmployeeComboBox.addItem("Select Employee");

        // Add all employees to the combo box
        addEmployeesToPromoteComboBox(managementSystem.getCeo());
    }

    private static void addEmployeesToPromoteComboBox(Employee employee) {
        promoteEmployeeComboBox.addItem(employee.getName());
        for (Employee subordinate : employee.getSubordinates()) {
            addEmployeesToPromoteComboBox(subordinate);
        }
    }
}
