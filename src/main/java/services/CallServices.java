package services;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CallServices {

    public static int countCallTime(String startTime, String endTime) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime startDate = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endTime, formatter);
        long timeDiff = Duration.between(startDate, endDate).toMillis();
        if (timeDiff < 0) {
            throw new IllegalArgumentException("End time is earlier than start time.");
        }
        return (int) Math.ceil(timeDiff / 1000.0 / 60.0);
    }

    public static double countCallCost(String tariff, int minutesCounter, int callTime, String type) {
        double callCost = 0.0;

        if (tariff.equals("06")) {
            if (callTime + minutesCounter <= 300) {
                callCost = 0.0;
            } else if (minutesCounter < 300) {
                callCost = callTime - (300 - minutesCounter);
            } else {
                callCost = callTime;
            }
        } else if (tariff.equals("03")) {
            callCost = callTime * 1.5;
        } else if (tariff.equals("11")) {
            if (type.equals("02")) {
                callCost = 0.0;
            } else if (callTime + minutesCounter <= 100) {
                callCost = callTime * 0.5;
            } else if (minutesCounter >= 100) {
                callCost = callTime * 1.5;
            } else {
                callCost = (100 - minutesCounter) * 0.5 + (callTime - (100 - minutesCounter)) * 1.5;
            }
        } else {
            throw new RuntimeException("Unexpected tariff.");
        }

        return callCost;
    }
}
