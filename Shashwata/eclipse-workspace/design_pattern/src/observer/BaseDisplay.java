package observer;

import javax.swing.*;

//Concrete Observer (BaseDisplay)
public class BaseDisplay implements Observer {
 private DecimalToBaseConverter subject;
 private JCheckBox binaryCheckBox;
 private JCheckBox octalCheckBox;
 private JCheckBox hexCheckBox;
 private JLabel binaryLabel;
 private JLabel octalLabel;
 private JLabel hexLabel;

 // Constructor to initialize the observer
 public BaseDisplay(DecimalToBaseConverter subject, JCheckBox binaryCheckBox, JCheckBox octalCheckBox, JCheckBox hexCheckBox,
                    JLabel binaryLabel, JLabel octalLabel, JLabel hexLabel) {
     this.subject = subject;
     this.binaryCheckBox = binaryCheckBox;
     this.octalCheckBox = octalCheckBox;
     this.hexCheckBox = hexCheckBox;
     this.binaryLabel = binaryLabel;
     this.octalLabel = octalLabel;
     this.hexLabel = hexLabel;

     subject.addObserver(this);  // Register this observer with the subject
 }
 // Update method that gets called when the decimal value changes
 @Override
 public void update() {
     int decimalValue = subject.getDecimalValue();

     // Update the displays based on checkbox status
     if (binaryCheckBox.isSelected()) {
         binaryLabel.setText("Binary: " + Integer.toBinaryString(decimalValue));
     } else {
         binaryLabel.setText("Binary: ");
     }

     if (octalCheckBox.isSelected()) {
         octalLabel.setText("Octal: " + Integer.toOctalString(decimalValue));
     } else {
         octalLabel.setText("Octal: ");
     }

     if (hexCheckBox.isSelected()) {
         hexLabel.setText("Hexadecimal: " + Integer.toHexString(decimalValue).toUpperCase());
     } else {
         hexLabel.setText("Hexadecimal: ");
     }
 }
}

