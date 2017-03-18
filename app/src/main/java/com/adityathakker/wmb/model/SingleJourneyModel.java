package com.adityathakker.wmb.model;


import java.util.Comparator;

/**
 * Created by adityajthakker on 17/8/16.
 */
public class SingleJourneyModel {
    private String sourceName;
    private String destinationName;
    private int sourceId;
    private int destinationId;
    private String busNumber;
    private int stops;

    public SingleJourneyModel(String sourceName, String destinationName, int sourceId, int destinationId, String busNumber, int stops) {
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.busNumber = busNumber;
        this.stops = stops;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public int getStops() {
        return stops;
    }

    public String toString(){
        return "Source: " + sourceName + " Destination: " + destinationName + " Bus No: " + busNumber + " Stops: " + stops;
    }


}
