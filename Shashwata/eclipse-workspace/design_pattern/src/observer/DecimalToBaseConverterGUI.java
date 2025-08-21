package observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DecimalToBaseConverterGUI {
    private JFrame frame;
    private JTextField decimalInputField;
    private JLabel binaryLabel;
    private JLabel octalLabel;
    private JLabel hexLabel;
    private JCheckBox binaryCheckBox;
    private JCheckBox octalCheckBox;
    private JCheckBox hexCheckBox;
    private DecimalToBaseConverter decimalToBaseConverter;

    // Constructor to initialize the GUI components
    public DecimalToBaseConverterGUI() {
        decimalToBaseConverter = new DecimalToBaseConverter();

        // Frame setup
        frame = new JFrame("Decimal to Binary/Octal/Hex Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6,1,20,25));

        // Decimal input field
        decimalInputField = new JTextField();
        decimalInputField.setToolTipText("Enter decimal number");
        decimalInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    int decimalValue = Integer.parseInt(decimalInputField.getText());
                    decimalToBaseConverter.setDecimalValue(decimalValue);
                } catch (NumberFormatException ex) {
                    // Handle invalid input
                    decimalToBaseConverter.setDecimalValue(0);
                }
            }
        });

        // Checkboxes
        binaryCheckBox = new JCheckBox("Show Binary");
        octalCheckBox = new JCheckBox("Show Octal");
        hexCheckBox = new JCheckBox("Show Hexadecimal");

        // Labels for the output
        binaryLabel = new JLabel("Binary: ");
        octalLabel = new JLabel("Octal: ");
        hexLabel = new JLabel("Hexadecimal: ");

        // Add components to the frame
        frame.add(new JLabel("Enter Decimal Number:"));
        frame.add(decimalInputField);
        frame.add(binaryCheckBox);
        frame.add(octalCheckBox);
        frame.add(hexCheckBox);
        frame.add(binaryLabel);
        frame.add(octalLabel);
        frame.add(hexLabel);

        // Set window properties
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Initialize the observers
        new BaseDisplay(decimalToBaseConverter, binaryCheckBox, octalCheckBox, hexCheckBox, binaryLabel, octalLabel, hexLabel);
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DecimalToBaseConverterGUI());
    }
}

