package org.wwu.dma.nosql.data;

public class BillingInformation {
    private static int nextId = 100;
    private int id;
    private final String bic;
    private final String iban;

    public BillingInformation(String bic, String iban) {
        this(nextId++, bic, iban);
    }

    public BillingInformation(int id, String bic, String iban) {
        this.id = id;
        this.bic = bic;
        this.iban = iban;
    }

    public int getId() {
        return id;
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }

    public String toString() {
        return "BIC: " + this.bic + ", IBAN: " + this.iban;
    }
}
