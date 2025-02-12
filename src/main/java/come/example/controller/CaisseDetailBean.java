package come.example.controller;




import java.io.Serializable;


import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;



@Named
@SessionScoped
public class CaisseDetailBean implements Serializable {
    private Map<Double, Integer> ticketCounts;
    private double totalAmount;

    public CaisseDetailBean() {
        ticketCounts = new LinkedHashMap<>();
        Double[] denominations = {2000.0, 1000.0, 500.0, 200.0, 100.0, 50.0, 20.0, 10.0, 5.0, 2.0, 1.0, 0.5};
        for (Double denomination : denominations) {
            ticketCounts.put(denomination, null);
        }
        totalAmount = 0;
    }

    public void updateTotal() {
        // Reset total amount before recalculating
        totalAmount = 0;
        
        for (Map.Entry<Double, Integer> entry : ticketCounts.entrySet()) {
            try {
                Double denomination = entry.getKey();
                Object quantityObj = entry.getValue();
                
                // Safe conversion
                Integer quantity = null;
                if (quantityObj instanceof Integer) {
                    quantity = (Integer) quantityObj;
                } else if (quantityObj instanceof String) {
                    quantity = Integer.parseInt((String) quantityObj);
                }
                
                // Calculate total if quantity is valid
                if (quantity != null) {
                    totalAmount += denomination * quantity;
                }
            } catch (Exception e) {
                // Log or handle the conversion error
                System.err.println("Error converting quantity: " + e.getMessage());
            }
        }
    }

    public void save() {
        System.out.println("Saved: " + ticketCounts + " | Total: " + totalAmount);
    }

    public Map<Double, Integer> getTicketCounts() {
        return ticketCounts;
    }

    public void setTicketCounts(Map<Double, Integer> ticketCounts) {
        if (ticketCounts != null) {
            this.ticketCounts = ticketCounts;
            updateTotal();
        }
    }

    public void onQuantityChange() {
        updateTotal();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    // Add a method to safely set a quantity
    public void setQuantity(Double denomination, Object value) {
        try {
            Integer quantity;
            if (value instanceof String) {
                quantity = Integer.parseInt((String) value);
            } else if (value instanceof Integer) {
                quantity = (Integer) value;
            } else {
                quantity = null;
            }
            ticketCounts.put(denomination, quantity);
            updateTotal();
        } catch (Exception e) {
            ticketCounts.put(denomination, 0);
            updateTotal();
        }
    }
}