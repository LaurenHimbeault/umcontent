import java.util.*;

public class OrderProcessor52 {

    private Map<String, Double> taxRates = new HashMap<>();

    public OrderProcessor() {
        taxRates.put("CA", 0.075);
        taxRates.put("NY", 0.085);
        taxRates.put("TX", 0.065);
    }

    public double calculateTotal(double price, int quantity, String state) {
        double subtotal = price * quantity;

        if (state.equals("CA")) {
            subtotal += subtotal * 0.075;
        } else if (state.equals("NY")) {
            subtotal += subtotal * 0.085;
        } else if (state.equals("TX")) {
            subtotal += subtotal * 0.065;
        }

        return subtotal;
    }

    public void processOrder(String customerId, double price, int quantity, String state) {
        double total = calculateTotal(price, quantity, state);
        String confirmation = "Order for customer " + customerId + ": $" + total;
        logToConsole(confirmation);
        saveToDatabase("customer_id=" + customerId + "&total=" + total);
    }

    public void processTestOrder(String testId, double testPrice, int testQuantity, String testState) {
        double total = calculateTotal(testPrice, testQuantity, testState);
        String confirmation = "Order for customer " + testId + ": $" + total;
        logToConsole(confirmation);
        saveToDatabase("customer_id=" + testId + "&total=" + total);
    }

    private void logToConsole(String message) {
        System.out.println("[LOG] " + message);
    }

    private void saveToDatabase(String data) {
        System.out.println("Pretend we're saving to DB: " + data);
    }
}
