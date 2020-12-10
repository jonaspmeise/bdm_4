package org.wwu.dma.nosql.data;

public class Order {
    private static int nextId = 100;
    private int id;
    private final String name;
    private final int amount;
    private final double price;

    public Order(String name, int amount, double price) {
        this(nextId++, name, amount, price);
    }

    public Order(int id, String name, int amount, double price) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public String toString() {
        return this.amount + " " + this.name + ": " + this.price + "€";
    }
}
