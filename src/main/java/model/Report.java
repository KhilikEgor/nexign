package model;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private final List<Call> calls = new ArrayList<>();
    private final String tariff;
    private final String phoneNumber;
    private int minCounter;
    public Report(String phoneNumber, String tariff) {
        this.minCounter = 0;
        this.phoneNumber = phoneNumber;
        this.tariff = tariff;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Call> getCalls() {
        return calls;
    }

    public int getMinCounter() {
        return minCounter;
    }

    public String getTariff() {
        return tariff;
    }

    public void setMinCounter(int minutes) {
        this.minCounter = minutes;
    }
}
