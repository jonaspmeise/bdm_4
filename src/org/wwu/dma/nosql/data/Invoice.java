package org.wwu.dma.nosql.data;

import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private static int nextId = 100;
    private int id;
    private Customer customer;
    private Supplier supplier;
    private List<Order> orders;

    public Invoice(Customer customer, Supplier supplier) {
        this(nextId++, customer, supplier, new ArrayList<Order>());
    }

    public Invoice(int id, Customer customer, Supplier supplier, List<Order> orders) {
        this.id = id;
        this.customer = customer;
        this.supplier = supplier;
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public String toString() {
        String result = "Supplier: " + this.supplier + "\nCustomer: " + this.customer + "\nOrders:\n";

        for(Order o: this.orders) {
            result += "\t" + o + "\n";
        }

        return result;
    }
}
