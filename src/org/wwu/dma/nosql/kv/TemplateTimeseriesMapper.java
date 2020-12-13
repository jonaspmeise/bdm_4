package org.wwu.dma.nosql.kv;

import org.bson.Document;
import org.wwu.dma.nosql.data.Customer;
import org.wwu.dma.nosql.data.Invoice;
import org.wwu.dma.nosql.data.Order;
import org.wwu.dma.nosql.data.Supplier;
import org.wwu.dma.nosql.data.TimeSeriesPoint;
import org.wwu.dma.nosql.data.TimeseriesDataGenerator;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import redis.clients.jedis.Jedis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TemplateTimeseriesMapper {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println("Connected to Redis");
        
        //MONGO DB SETUP sodass wir die Daten dort auch abspeichern können
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> collection = database.getCollection("timeseries");
		collection.drop();
        
        
        Random rand = new Random();
        rand.setSeed(1234L);
        TimeSeriesPoint[] data;
        long seed;
        for(int i = 0; i < 100; i++) {
            seed = rand.nextLong();
            data = TimeseriesDataGenerator.generateTimeSeriesExampleData(100, seed);
            TemplateTimeseriesMapper.saveTimeseries(jedis, i, data);
            TemplateTimeseriesMapper.saveTimeseries(collection, i, data);
        }

        double highest_value = Double.MIN_VALUE;
        Date highest_value_timestamp = null;
        int highest_value_tsId = -1;
        
        int[] highestValuePair = highestValue(jedis);
        TimeSeriesPoint highestValuePoint = fetchTimeSeriesPoint(jedis, highestValuePair[0], highestValuePair[1]);
        
        highest_value = highestValuePoint.getValue();
        highest_value_timestamp = highestValuePoint.getTimestamp();
        highest_value_tsId = highestValuePair[0];

        System.out.println("Highest value (" + highest_value + ") measured on " + highest_value_timestamp +
                " as part of time series #" + highest_value_tsId);
        
        //
        //MONGO DB PART
        //
		
		mongoClient.close();
    }

    public static void saveTimeseries(Jedis jedis, int id, TimeSeriesPoint[] timeSeries) {
    	//Straighte Implementation: timeseries:id -> timeSeries.length , wobei über timeSeries.length die einzelnen TimeSeriesPoints angesteuert werden können:
    	//timeseries:id:1 	-> HashMap mit beiden Values
    	//timeseries:id:2	"
    	//timeseries:id:3	"
    	//....

    	jedis.set("timeseries:" + id, Integer.toString(timeSeries.length));
    	for(int i=0;i < timeSeries.length;i++) {
    		TimeSeriesPoint timeSeriesPoint = timeSeries[i];
    		
    		HashMap<String, String> timeSeriesPointMap = new HashMap<String, String>();
    		timeSeriesPointMap.put("date", TimeSeriesPoint.formatter.format(timeSeriesPoint.getTimestamp()));
    		timeSeriesPointMap.put("value", Double.toString(timeSeriesPoint.getValue()));
    		
    		
    		jedis.hmset("timeseries:" + id + ":" + (i+1), timeSeriesPointMap);
    	}
    }
    
    public static void saveTimeseries(MongoCollection<Document> collection, int id, TimeSeriesPoint[] timeSeries) {
    	Document doc = new Document("_id", id)
    			.append("timeseriespoints", getTimeseriesDocuments(timeSeries));
    	collection.insertOne(doc);
    }
    
    public static Document getTimeseriesDocuments(TimeSeriesPoint[] timeSeries) {
    	if (timeSeries.length >= 1) {
    		Document returnDocument = new Document("timeseriespoint", getTimeseriesDocument(timeSeries[0])); 
    			
    		for(int i=1;i<timeSeries.length;i++) {
    			returnDocument.append(Integer.toString(i), getTimeseriesDocument(timeSeries[i]));
    		}
    		
    		return returnDocument;
    	} else {
    		return null;
    	}
    }
    
    public static Document getTimeseriesDocument(TimeSeriesPoint timeSeriesPoint) {
    	return new Document("timestamp", timeSeriesPoint.getTimestamp())
    			.append("value", timeSeriesPoint.getValue());
    }
    
    //return Array[2] with [0]=ID of timeseries, [1]=point within timeseries
    public static int[] highestValue(Jedis jedis) {
    	int[] foundTimeSeriesPoint = new int[2];
    	double highestValue = Double.MIN_VALUE;
    	//iteriere über alle IDs - wir wissen, was unsere Start-ID/niedrigste ID ist und cyclen dann solange durch alle IDs, bis wir keine Treffer mehr für ein Objekt kriegen
    	int currentID = 0;
    	
    	String numberMembers = jedis.get("timeseries:" + Integer.toString(currentID));
    	
    	while(numberMembers!=null) {
        	//search for all TimeSeriesPoints
    		for(int i=1;i<=Integer.valueOf(numberMembers);i++) {
    			//Wir benutzen .get(0) hier, da unsere hashMap für jeden key nur einen Value zuordnet, hmget() allerdings eine List<String> zurückgibt, welche dann halt nur 1 Element enthält
    			double comparisonValue = Double.valueOf(jedis.hmget("timeseries:" + currentID + ":" + i, "value").get(0));
    			
    			if(comparisonValue > highestValue) {
    				highestValue = comparisonValue;
    				foundTimeSeriesPoint[0] = Integer.valueOf(currentID);
    				foundTimeSeriesPoint[1] = i;
    				
    				//System.out.println("Current highest value found in TimeSeries#" + foundTimeSeriesPoint[0] + " @Point#" + i + " with value " + highestValue);
    			}
    		}
    		
    		//nächste TimeSeries suchen
    		currentID++;
    		numberMembers = jedis.get("timeseries:" + Integer.toString(currentID));
    	}

    	return foundTimeSeriesPoint;
    	
    }
    
    public static TimeSeriesPoint fetchTimeSeriesPoint(Jedis jedis, int id, int positionInSeries) {
		Date currentDateValue = null;
    	
		try {
			//.get(0) weil wegen siehe int[] highestValue
			currentDateValue = TimeSeriesPoint.formatter.parse(jedis.hmget("timeseries:" + id + ":" + positionInSeries, "date").get(0));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return new TimeSeriesPoint(currentDateValue, Double.valueOf(jedis.hmget("timeseries:" + id + ":" + positionInSeries, "value").get(0)));
    }

    /*
     * You may add further methods HERE to structure your procedures for time series saving, querying and possibly retrieval.
     */
}
