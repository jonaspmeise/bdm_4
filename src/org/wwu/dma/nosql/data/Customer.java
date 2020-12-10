package org.wwu.dma.nosql.data;

public class Customer {
    private static int nextId = 100;
    private int id;
    private final String name;
    private final Address address;

    public Customer(String name, Address address) {
        this(nextId++, name, address);
    }

    public Customer(int id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public String toString() {
        return this.name + ", " + this.address;
    }
}
