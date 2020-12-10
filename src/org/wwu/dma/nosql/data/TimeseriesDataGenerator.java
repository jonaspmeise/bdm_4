package org.wwu.dma.nosql.data;

import java.util.Date;
import java.util.Random;

public class TimeseriesDataGenerator {
    public static TimeSeriesPoint[] generateTimeSeriesExampleData(int dataPoints, long seed) {
        Random rand = new Random();
        rand.setSeed(seed);

        TimeSeriesPoint[] timeSeries = new TimeSeriesPoint[dataPoints];

        double value = rand.nextDouble();
        long ticks = new Date(1475272800000L).getTime();
        TimeSeriesPoint ts;

        for(int i = 0; i < timeSeries.length; i++) {
            value = value + (rand.nextGaussian() * 5);
            ticks = rand.nextInt(5000000) + ticks;
            ts = new TimeSeriesPoint(new Date(ticks), value);
            timeSeries[i] = ts;
        }

        return timeSeries;
    }
}
