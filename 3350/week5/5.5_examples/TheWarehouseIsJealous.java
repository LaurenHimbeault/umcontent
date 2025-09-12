import java.util.List;

class Address {
    final String street, city, countryCode, postalCode;
    Address(String street, String city, String countryCode, String postalCode) {
        this.street = street; this.city = city; this.countryCode = countryCode; this.postalCode = postalCode;
    }
}

class LineItem {
    final String sku; final int qty; final double unitWeightKg;
    LineItem(String sku, int qty, double unitWeightKg) { this.sku = sku; this.qty = qty; this.unitWeightKg = unitWeightKg; }
}

class Order {
    final Address shipTo;
    final List<LineItem> items;
    final double boxLengthCm, boxWidthCm, boxHeightCm; // packaging dims picked earlier
    Order(Address shipTo, List<LineItem> items, double L, double W, double H) {
        this.shipTo = shipTo; this.items = items; this.boxLengthCm = L; this.boxWidthCm = W; this.boxHeightCm = H;
    }
}

class TheWarehouseIsJealous {
    // FEATURE ENVY: This method fixates on Order and Address internals.
    double estimateShipping(Order order) {
        // compute real weight
        double actualKg = 0.0;
        for (LineItem li : order.items) {
            actualKg += li.unitWeightKg * li.qty;
        }
        // compute dimensional weight (cm^3 / 5000 rule of thumb)
        double dimKg = (order.boxLengthCm * order.boxWidthCm * order.boxHeightCm) / 5000.0;

        // choose billable weight
        double billableKg = Math.max(actualKg, dimKg);

        // map destination → zone (hardcoded + poking into address)
        String zone;
        if ("US".equals(order.shipTo.countryCode)) {
            zone = order.shipTo.postalCode.startsWith("9") ? "Z8" : "Z5";
        } else if ("CA".equals(order.shipTo.countryCode)) {
            zone = order.shipTo.postalCode.startsWith("R") ? "Z7" : "Z6";
        } else {
            zone = "INTL";
        }

        // look up base rate by zone (also doesn’t belong here)
        double base;
        switch (zone) {
            case "Z8": base = 10.0; break;
            case "Z7": base = 12.0; break;
            case "Z6": base = 14.0; break;
            case "Z5": base = 11.0; break;
            default: base = 25.0;
        }

        // surcharge if city looks remote (more poking)
        boolean remote = order.shipTo.city.toLowerCase().contains("bay") || order.shipTo.city.toLowerCase().contains("harbor");
        double remoteSurcharge = remote ? 6.0 : 0.0;

        // final price
        return base + billableKg * 1.8 + remoteSurcharge;
    }
}
