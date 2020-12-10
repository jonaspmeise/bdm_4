package org.wwu.dma.nosql.data;

public class Address {
    private static int nextId = 100;
    private int id;
    private final int zipCode;
    private final String city;
    private final String street;
    private final int streetNumber;

    public Address(int zipCode, String city, String street, int streetNumber) {
        this(nextId++, zipCode, city, street, streetNumber);
    }

    public Address(int id, int zipCode, String city, String street, int streetNumber) {
        this.id = id;
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
    }

    public int getId() {
        return id;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public String toString() {
        return this.street + " " + this.streetNumber + ", " + this.zipCode + " " + this.city;
    }
}
