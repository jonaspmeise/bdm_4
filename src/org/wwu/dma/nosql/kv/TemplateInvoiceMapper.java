package org.wwu.dma.nosql.kv;

import org.wwu.dma.nosql.data.*;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemplateInvoiceMapper {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println("Connected to Redis");

        Invoice[] invoices = InvoiceDataGenerator.generateAllInvoiceExamples();

        List<Integer> ids = new ArrayList<Integer>();

        for(Invoice invoice: invoices) {
            TemplateInvoiceMapper.saveInvoice(jedis, invoice);
            ids.add(invoice.getId());
        }

        double highest_single_prize = -1;
        String highest_single_prize_name = "";

        /*
         * TODO
         * Task 5.2c)
         * Implement your procedures for querying REDIS for the highest prize per unit and the corresponding unit's name HERE.
         */

        System.out.println("Highest Single Prize: " + highest_single_prize_name + " at " + highest_single_prize + "â‚¬.");
    }

    public static void saveInvoice(Jedis jedis, Invoice invoice) {
        /*
         * TODO
         * Task 5.2a)
         * Implement procedures for saving invoices HERE!
         */
    	//Basic Invoice Information in customerSupplier-HashMap und auf ID von invoice legen
    	Customer currentCustomer = invoice.getCustomer();
    	Supplier currentSupplier = invoice.getSupplier();
    	Address currentAddress_customer = currentCustomer.getAddress();
    	Address currentAddress_supplier = currentCustomer.getAddress();
    	BillingInformation currentBillingInformation = currentSupplier.getBillingInformation();
    	
    	HashMap<String, String> customerSupplier = new HashMap<String, String>();
    	customerSupplier.put("customer", Integer.toString(currentCustomer.getId()));
    	customerSupplier.put("supplier", Integer.toString(currentSupplier.getId()));
    	
    	jedis.hmset("invoice:" + invoice.getId(), customerSupplier);
    	
    	//In invoice:invoice.id:order ablegen, wieviele Orders es insgesamt gibt
    	//Zugriff auf einzelne Orders erfolgt dann über Hashes auf den einzelnen Ordereinträgen 
    	//		invoice:invoice.id:order:1
    	//		invoice:invoice.id:order:2
    	//		invoice:invoice.id:order:3
    	//		....
    	jedis.set("invoice:" + invoice.getId() + ":order", Integer.toString(invoice.getOrders().size()));
    	
    	//Eine Invoice ohne Order kann nicht existieren -> kein Sanitycheck notwendig
    	for(int i = 0;i<invoice.getOrders().size();i++) {
    		//Platziere HashMap mit Order-Einträgen (ID, name, amount, price)
    		Order currentOrder = invoice.getOrders().get(i);
    		
    		HashMap<String, String> orderMap = new HashMap<String, String>();
    		orderMap.put("id", Integer.toString(currentOrder.getId()));
    		orderMap.put("name", currentOrder.getName());
    		orderMap.put("amount", Integer.toString(currentOrder.getAmount()));
    		orderMap.put("price", Double.toString(currentOrder.getPrice()));
    		
    		jedis.hmset("invoice:" + invoice.getId() + ":order:" + (i+1), orderMap);
    	}
    	
    	//Informationen von Customer:
    	//customer:customer.id -> name
    	jedis.set("customer:" + currentCustomer.getId(), currentCustomer.getName());
    	
    	//customer:customer.id:address -> HashMap mit den 4 Dateneinträgen innerhalb von Address
    	HashMap<String, String> addressMap_customer = new HashMap<String, String>();
    	addressMap_customer.put("id", Integer.toString(currentAddress_customer.getId()));
    	addressMap_customer.put("zipcode", Integer.toString(currentAddress_customer.getZipCode()));
    	addressMap_customer.put("city", currentAddress_customer.getCity());
    	addressMap_customer.put("street", currentAddress_customer.getStreet());
    	addressMap_customer.put("streetnumber", Integer.toString(currentAddress_customer.getStreetNumber()));
    	
    	jedis.hmset("customer:" + currentCustomer.getId() + ":address", addressMap_customer);
    	
    	//Informationen von Supplier:
    	//supplier:supplier.id -> name
    	jedis.set("supplier:" + currentSupplier.getId(), currentSupplier.getName());
    	
    	//supplier:supplier.id.address -> HashMap mit den 4 Dateneinträgen innerhalb von Address
    	HashMap<String, String> addressMap_supplier = new HashMap<String, String>();
    	addressMap_supplier.put("id", Integer.toString(currentAddress_supplier.getId()));
    	addressMap_supplier.put("zipcode", Integer.toString(currentAddress_supplier.getZipCode()));
    	addressMap_supplier.put("city", currentAddress_supplier.getCity());
    	addressMap_supplier.put("street", currentAddress_supplier.getStreet());
    	addressMap_supplier.put("streetnumber", Integer.toString(currentAddress_supplier.getStreetNumber()));
    	
    	jedis.hmset("supplier:" + currentSupplier.getId() + ":address", addressMap_supplier);
    	
    	//supplier:supplier.id.billinginformation -> HashMap mit den 3 Dateneinträgen innerhalb von BillingInformation
    	HashMap<String, String> billingInformationMap = new HashMap<String, String>();
    	billingInformationMap.put("id", Integer.toString(currentBillingInformation.getId()));
    	billingInformationMap.put("bic", currentBillingInformation.getBic());
    	billingInformationMap.put("iban", currentBillingInformation.getIban());
    	
    	jedis.hmset("supplier:" + currentSupplier.getId() + ":billinginformation", billingInformationMap);
    }

    /*
     * You may add further methods HERE to structure your procedures for invoice saving, querying and possibly retrieval.
     */
}
