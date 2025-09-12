enum PaymentType {
    CREDIT_CARD, PAYPAL, BANK_TRANSFER
}

class TheOldSwitcheroo {

    //Lots of switches based on PaymentType
    double calculateFee(PaymentType type, double amount) {
        switch (type) {
            case CREDIT_CARD:
                return amount * 0.03 + 0.30; // 3% + $0.30
            case PAYPAL:
                return amount * 0.029 + 0.35; // 2.9% + $0.35
            case BANK_TRANSFER:
                return 1.00; // flat fee
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    String generateReceipt(PaymentType type, double amount) {
        if (type == PaymentType.CREDIT_CARD) {
            return "Paid $" + amount + " by Credit Card";
        } else if (type == PaymentType.PAYPAL) {
            return "Paid $" + amount + " via PayPal";
        } else if (type == PaymentType.BANK_TRANSFER) {
            return "Paid $" + amount + " by Bank Transfer";
        } else {
            return "Unknown payment";
        }
    }

    boolean isRefundable(PaymentType type) {
        if (type == PaymentType.CREDIT_CARD) return true;
        if (type == PaymentType.PAYPAL) return true;
        if (type == PaymentType.BANK_TRANSFER) return false;
        return false;
    }
}
