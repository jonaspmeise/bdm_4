package org.wwu.dma.nosql.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeSeriesPoint {
    private final Date timestamp;
    private final double value;
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss");

    public TimeSeriesPoint(Date timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }

    public String toString() {
        return "value: " + this.value + ", time: " + TimeSeriesPoint.formatter.format(this.timestamp);
    }
}
