package org.wwu.dma.nosql.data;

public class Supplier {
    private static int nextId = 100;
    private int id;
    private final String name;
    private final Address address;
    private final BillingInformation billingInformation;

    public Supplier(String name, Address address, BillingInformation billingInformation) {
        this(nextId++, name, address, billingInformation);
    }

    public Supplier(int id, String name, Address address, BillingInformation billingInformation) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.billingInformation = billingInformation;
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

    public BillingInformation getBillingInformation() {
        return billingInformation;
    }

    public String toString() {
        return this.name + ", " + this.address + ",\n\t" + this.billingInformation;
    }
}
