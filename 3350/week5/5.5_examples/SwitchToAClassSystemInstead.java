interface PaymentMethod {
    double calculateFee(double amount);
    String generateReceipt(double amount);
    boolean isRefundable();
}

class CreditCardPayment implements PaymentMethod {
    public double calculateFee(double amount) { return amount * 0.03 + 0.30; }
    public String generateReceipt(double amount) { return "Paid $" + amount + " by Credit Card"; }
    public boolean isRefundable() { return true; }
}

class PaypalPayment implements PaymentMethod {
    public double calculateFee(double amount) { return amount * 0.029 + 0.35; }
    public String generateReceipt(double amount) { return "Paid $" + amount + " via PayPal"; }
    public boolean isRefundable() { return true; }
}

class BankTransferPayment implements PaymentMethod {
    public double calculateFee(double amount) { return 1.00; }
    public String generateReceipt(double amount) { return "Paid $" + amount + " by Bank Transfer"; }
    public boolean isRefundable() { return false; }
}

// Example usage with a HashMap (dependency injection style)
class PaymentRegistry {
    private final Map<String, PaymentMethod> methods = new HashMap<>();

    PaymentRegistry() {
        methods.put("card", new CreditCardPayment());
        methods.put("paypal", new PaypalPayment());
        methods.put("bank", new BankTransferPayment());
    }

    PaymentMethod get(String key) {
        return methods.get(key);
    }
}

public class SwitchToAClassSystemInstead {
    public static void main(String[] args) {
        PaymentRegistry registry = new PaymentRegistry();
        PaymentMethod method = registry.get("paypal");

        double amount = 100.0;
        System.out.println("Fee: " + method.calculateFee(amount));
        System.out.println(method.generateReceipt(amount));
        System.out.println("Refundable? " + method.isRefundable());
    }
}
