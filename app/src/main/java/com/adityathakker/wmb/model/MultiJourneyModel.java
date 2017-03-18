package com.adityathakker.wmb.model;

/**
 * Created by adityajthakker on 17/8/16.
 */
public class MultiJourneyModel {
    private String sourceName;
    private String destinationName;
    private String middleName;
    private int sourceId;
    private int middleId;
    private int destinationId;
    private String startingBusNumber;
    private String endingBusNumber;
    private int stops;

    public MultiJourneyModel(String sourceName, String destinationName, String middleName, int sourceId, int middleId, int destinationId, String startingBusNumber, String endingBusNumber, int stops) {
        this.sourceName = sourceName;
        this.destinationName = destinationName;
        this.middleName = middleName;
        this.sourceId = sourceId;
        this.middleId = middleId;
        this.destinationId = destinationId;
        this.startingBusNumber = startingBusNumber;
        this.endingBusNumber = endingBusNumber;
        this.stops = stops;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getMiddleId() {
        return middleId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public String getStartingBusNumber() {
        return startingBusNumber;
    }

    public String getEndingBusNumber() {
        return endingBusNumber;
    }

    public int getStops() {
        return stops;
    }

    public String toString(){
        return "Source: " + sourceName + " Middle: " + middleName + " Destination: " + destinationName + " Start Bus No: " + startingBusNumber + " End Bus No: " + endingBusNumber + " Stops: " + stops;
    }
}
