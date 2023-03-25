package model;

import services.CallServices;

import java.text.ParseException;

public class Call {

    private final String type;
    private final String startTime;
    private final String endTime;
    private final int callTime;
    private final double cost;

    public Call(String type, String startTime, String endTime, String tariff, int minutesCounter) throws ParseException {
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.callTime = CallServices.countCallTime(startTime, endTime);
        this.cost = CallServices.countCallCost(tariff, minutesCounter, this.callTime, this.type);
    }

    public int getCallTime() {
        return callTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public double getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }
}
