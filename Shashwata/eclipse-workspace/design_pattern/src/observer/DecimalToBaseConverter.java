package observer;

import java.util.ArrayList;
import java.util.List;

// Subject class (DecimalToBaseConverter)
public class DecimalToBaseConverter {
    private int decimalValue;
    private List<Observer> observers = new ArrayList<>();

    // Set decimal value and notify observers
    public void setDecimalValue(int decimalValue) {
        this.decimalValue = decimalValue;
        notifyObservers();
    }

    // Get the current decimal value
    public int getDecimalValue() {
        return decimalValue;
    }

    // Add an observer to the list
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // Notify all observers to update
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
