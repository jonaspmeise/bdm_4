package org.wwu.dma.nosql.kv;

import org.wwu.dma.nosql.data.*;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

import redis.clients.jedis.Jedis;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TemplateInvoiceMapper {
    public static void main(String[] args) {
        //
        //JEDIS PART
        //
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

        Order highestPriceOrder = getHighestPriceOrder(jedis);
        highest_single_prize = highestPriceOrder.getPrice();
        highest_single_prize_name = highestPriceOrder.getName();

        System.out.println("Highest Single Prize: " + highest_single_prize_name + " at " + highest_single_prize + "â‚¬.");
        
        //
        //MONGO DB PART
        //
		
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> collection = database.getCollection("invoice");
		collection.drop();

		for(Invoice invoice : invoices) {
			TemplateInvoiceMapper.saveInvoice(collection, invoice);
		}
		
		FindIterable<Document> findIterable = collection.find()
				.sort(new BasicDBObject("price",-1))
				.limit(1)
				.projection(Projections.include("orders"));
		
		for(Document doc2 : findIterable) { 
			System.out.println(doc2);
		}
		 
		
		mongoClient.close();
		    
    }

    public static void saveInvoice(MongoCollection<Document> collection, Invoice invoice) {
    	Customer customer = invoice.getCustomer();
    	Supplier supplier = invoice.getSupplier();
    	List<Order> orders = invoice.getOrders();
    	
    	Document doc = new Document("_id", invoice.getId())
    			.append("customer", new Document("id", customer.getId())
    					.append("name", customer.getName())
    					.append("address", new Document("id", customer.getAddress().getId())
    							.append("zip", customer.getAddress().getZipCode())
    							.append("city", customer.getAddress().getCity())
    							.append("street", customer.getAddress().getStreet())
    							.append("number", customer.getAddress().getStreetNumber())))
    			.append("supplier", new Document("id", supplier.getId())
    					.append("name", supplier.getName())
    					.append("address", new Document("id", supplier.getAddress().getId())
    							.append("zip", supplier.getAddress().getZipCode())
    							.append("city", supplier.getAddress().getCity())
    							.append("street", supplier.getAddress().getStreet())
    							.append("number", supplier.getAddress().getStreetNumber()))
    					.append("billing",new Document("id", supplier.getBillingInformation().getId())
    							.append("iban",supplier.getBillingInformation().getIban())
    							.append("bic", supplier.getBillingInformation().getBic())))
    			.append("orders", getOrdersDoc(orders));
    	
    	collection.insertOne(doc);
    }
    
    public static void saveInvoice(Jedis jedis, Invoice invoice) {
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
    
    //Gibt die Order mit dem höchsten .getPrice() von allen Orders in jedis aus
    public static Order getHighestPriceOrder(Jedis jedis) {
    	Order returnOrder = null;
    	int currentInvoiceId = 100;
    	double highestPrice = Double.MIN_VALUE;
    	
    	//Plan: Iteriere über alle Invoices -> Iteriere über alle OrderListen -> Vergleiche Preise
    	//check, wieviele Orders in einem Invoice sind und dann iteriere darüber
    	
    	String numberOrders = jedis.get("invoice:" + currentInvoiceId + ":order");
    	
    	//Fliege raus wenn Invoice nicht mehr existiert für currentInvoiceId
    	if(numberOrders!=null) {
    		//Iteriere über alle Orders innerhalb von Invoice
    		
    		for(int i=1;i <= Integer.valueOf(numberOrders);i++) {
    			//.get(0) da wir in der HashMap jeweils laut Definition nur 1 Element pro Key zuweisen, und genau auf dieses Element zugreifen möchten
    			String currentElement = "invoice:" + currentInvoiceId + ":order:" + i;
    			double currentPrice = Double.valueOf(jedis.hmget(currentElement, "price").get(0));
    			
    			if(currentPrice > highestPrice) {
    				highestPrice = currentPrice;
    				returnOrder = new Order(Integer.valueOf(jedis.hmget(currentElement, "id").get(0)),
    						jedis.hmget(currentElement, "name").get(0),
    						Integer.valueOf(jedis.hmget(currentElement, "amount").get(0)),
    						Double.valueOf(jedis.hmget(currentElement, "price").get(0)));
    			}
    			
    		}
    		
    		
    		currentInvoiceId++;
    		numberOrders = jedis.get("invoice:" + currentInvoiceId + ":order");
    	}
    	  	
    	return returnOrder;
    }
      
    public static Document getOrdersDoc(List<Order> orders) {
    	Document returnDocument = null;
    	
    	if(orders.size() >= 1) {
    		//Erstes Order-Dokument initialisieren, damit wir alle anderen Dokumente daranhängen können
    		returnDocument = new Document("order",getOrderDoc(orders.get(0)));
    		
    		for(int i=1;i<orders.size();i++) {
    			returnDocument.append(Integer.toString(orders.get(i).getId()), getOrderDoc(orders.get(i)));
    		}
    	}
    	
    	return returnDocument;
    }
    
    public static Document getOrderDoc(Order order) {
    	return new Document("name",order.getName())
    			.append("price", order.getPrice())
    			.append("amount", order.getAmount());
    }
    
    /*
     * You may add further methods HERE to structure your procedures for invoice saving, querying and possibly retrieval.
     */
}
