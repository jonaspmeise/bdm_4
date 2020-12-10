package org.wwu.dma.nosql.data;

import java.text.DecimalFormat;
import java.util.Random;

public class InvoiceDataGenerator {

    public static Invoice[] generateAllInvoiceExamples() {
        Invoice[] invoices = new Invoice[5];
        invoices[0] = InvoiceDataGenerator.generateInvoiceExampleA();
        invoices[1] = InvoiceDataGenerator.generateInvoiceExampleB();
        invoices[2] = InvoiceDataGenerator.generateInvoiceExampleC();
        invoices[3] = InvoiceDataGenerator.generateInvoiceExampleD();
        invoices[4] = InvoiceDataGenerator.generateInvoiceExampleE();

        return invoices;
    }

    public static Invoice generateInvoiceExampleA() {
        Random rand = new Random();
        rand.setSeed(1234L);

        Address sAddress = new Address(45127, "Duisburg", "Leopoldstr.", 49);
        BillingInformation sBilling = new BillingInformation("DEUTDEBBXXX", "DE12 3456 0000 0000 7890");
        Supplier supplier = new Supplier("Mega Getraenkeladen", sAddress, sBilling);

        Address cAddress = new Address(78133, "Bruchsal", "Schobersmuehlenweg", 3);
        Customer customer = new Customer("Feierlaune Partyservice", cAddress);

        String[] names = {"Alkoholfreies Weizen", "Tomaten", "Knoblauch", "TK-Pizza", "Teepackung"};

        Invoice invoice = new Invoice(customer, supplier);

        int amount;
        double price;
        Order o;

        for (int i = 0; i < names.length; i++) {
            amount = rand.nextInt(10);
            price = Math.abs(10.0 + 4 * rand.nextGaussian());
            price = Math.round(amount * price * 100) / 100.0;
            o = new Order(names[i], amount, price);
            invoice.addOrder(o);
        }

        return invoice;
    }

    public static Invoice generateInvoiceExampleB() {
        Random rand = new Random();
        rand.setSeed(1234L);

        Address sAddress = new Address(48127, "Münster", "Engelstraße", 18);
        BillingInformation sBilling = new BillingInformation("DEUTDEBBXXX", "DE12 3456 0000 0000 1569");
        Supplier supplier = new Supplier("Supermarkt Lecker Zeugs", sAddress, sBilling);

        Address cAddress = new Address(81379, "Hamburg", "Eichrodtweg", 29);
        Customer customer = new Customer("Peter Petersen", cAddress);

        String[] names = {"Tomaten", "Oliven", "Eisbergsalat", "Sylter Salatdressing"};

        Invoice invoice = new Invoice(customer, supplier);

        int amount;
        double price;
        Order o;

        for (int i = 0; i < names.length; i++) {
            amount = rand.nextInt(10);
            price = Math.abs(10.0 + 4 * rand.nextGaussian());
            price = Math.round(amount * price * 100) / 100.0;
            o = new Order(names[i], amount, price);
            invoice.addOrder(o);
        }

        return invoice;
    }

    public static Invoice generateInvoiceExampleC() {
        Random rand = new Random();
        rand.setSeed(1234L);

        Address sAddress = new Address(46389, "Bad Doberan", "Ettlinger Str.", 784);
        BillingInformation sBilling = new BillingInformation("DEUTDEBBXXX", "DE12 3456 0000 0000 7890");
        Supplier supplier = new Supplier("IT Laden", sAddress, sBilling);

        Address cAddress = new Address(97124, "Berlin", "Gartenstr.", 17);
        Customer customer = new Customer("Karin Burke", cAddress);

        String[] names = {"HDMI-Kabel", "SSDs", "Mehrfachsteckdosenleiste"};

        Invoice invoice = new Invoice(customer, supplier);

        int amount;
        double price;
        Order o;

        for (int i = 0; i < names.length; i++) {
            amount = rand.nextInt(10);
            price = Math.abs(10.0 + 4 * rand.nextGaussian());
            price = Math.round(amount * price * 100) / 100.0;
            o = new Order(names[i], amount, price);
            invoice.addOrder(o);
        }

        return invoice;
    }

    public static Invoice generateInvoiceExampleD() {
        Random rand = new Random();
        rand.setSeed(1234L);

        Address sAddress = new Address(25473, "Kiel", "Eichrodtweg", 14);
        BillingInformation sBilling = new BillingInformation("DEUTDEBBXXX", "DE12 3456 0000 0000 1234");
        Supplier supplier = new Supplier("St.-Patricks-Day Kostümverleih", sAddress, sBilling);

        Address cAddress = new Address(12437, "Rostock", "Einsteinstr.", 61);
        Customer customer = new Customer("Hauke Holgerson", cAddress);

        String[] names = {"Grünes Guinness", "Riesen-Partyhut"};

        Invoice invoice = new Invoice(customer, supplier);

        int amount;
        double price;
        Order o;

        for (int i = 0; i < names.length; i++) {
            amount = rand.nextInt(10);
            price = Math.abs(10.0 + 4 * rand.nextGaussian());
            price = Math.round(amount * price * 100) / 100.0;
            o = new Order(names[i], amount, price);
            invoice.addOrder(o);
        }

        return invoice;
    }

    public static Invoice generateInvoiceExampleE() {
        Random rand = new Random();
        rand.setSeed(1234L);

        Address sAddress = new Address(87634, "Nürnberg", "Saskatchewan-Weg", 1);
        BillingInformation sBilling = new BillingInformation("DEUTDEBBXXX", "DE12 1256 0000 0000 5328");
        Supplier supplier = new Supplier("Langweilige Bueroartikel", sAddress, sBilling);

        Address cAddress = new Address(91133, "Traben-Trarbach", "Bernkastel-Kues-Str.", 75);
        Customer customer = new Customer("Alfred Hammes", cAddress);

        String[] names = {"Tacker", "Locher", "Bleistift", "Textmarker", "Radierer", "Büroklammern"};

        Invoice invoice = new Invoice(customer, supplier);

        int amount;
        double price;
        Order o;

        for (int i = 0; i < names.length; i++) {
            amount = rand.nextInt(10);
            price = Math.abs(10.0 + 4 * rand.nextGaussian());
            price = Math.round(amount * price * 100) / 100.0;
            o = new Order(names[i], amount, price);
            invoice.addOrder(o);
        }

        return invoice;
    }
}
