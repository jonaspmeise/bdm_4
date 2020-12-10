package org.wwu.dma.nosql.kv;

import org.wwu.dma.nosql.data.TimeSeriesPoint;
import org.wwu.dma.nosql.data.TimeseriesDataGenerator;
import redis.clients.jedis.Jedis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class TemplateTimeseriesMapper {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println("Connected to Redis");

        Random rand = new Random();
        rand.setSeed(1234L);
        TimeSeriesPoint[] data;
        long seed;
        for(int i = 0; i < 100; i++) {
            seed = rand.nextLong();
            data = TimeseriesDataGenerator.generateTimeSeriesExampleData(100, seed);
            TemplateTimeseriesMapper.saveTimeseries(jedis, i, data);
        }

        double highest_value = Double.MIN_VALUE;
        Date highest_value_timestamp = null;
        int highest_value_tsId = -1;

        /*
         * TODO
         * Task 5.2b)
         * Implement your procedures for querying REDIS for the highest value of all time series' here.
         */

        System.out.println("Highest value (" + highest_value + ") measured on " + highest_value_timestamp +
                " as part of time series #" + highest_value_tsId);
    }

    public static void saveTimeseries(Jedis jedis, int id, TimeSeriesPoint[] timeSeries) {
        /*
         * TODO
         * Task 5.2a)
         * Implement procedures for saving time series data HERE!
         */
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
    		
    		
    		jedis.hmset("timeseries" + id + ":" + (i+1), timeSeriesPointMap);
    	}
    }

    /*
     * You may add further methods HERE to structure your procedures for time series saving, querying and possibly retrieval.
     */
}
