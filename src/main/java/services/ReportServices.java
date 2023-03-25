package services;

import model.Call;
import model.Report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReportServices {

    private static final String BORDER = "-".repeat(76) + "\n";
    private static final String ATTRIBUTES =
            "| Call Type |   Start Time        |     End Time        | Duration | Cost  |\n";

    private static void createReportFile(Report report) throws IOException, ParseException {
        String filename = String.format("./reports/%s.txt", report.getPhoneNumber());
        try (FileOutputStream file = new FileOutputStream(filename)) {
            file.write(String.format("Tariff index: %s\n", report.getTariff()).getBytes());
            file.write(BORDER.getBytes());
            file.write(String.format("Report for phone number %s:\n", report.getPhoneNumber()).getBytes());
            file.write(BORDER.getBytes());
            file.write(ATTRIBUTES.getBytes());
            file.write(BORDER.getBytes());

            List<Call> calls = new ArrayList<>(report.getCalls());
            calls.sort(Comparator.comparing(Call::getType).thenComparing(Call::getStartTime));

            double totalCost = report.getTariff().equals("06") ? 100.0 : 0;
            for (Call call : calls) {
                writeCallToFile(call, file);
                totalCost += call.getCost();
            }

            file.write(BORDER.getBytes());
            file.write(String.format("|                                           Total Cost: |     %.2f rubles |\n", totalCost)
                    .replace(",", ".").getBytes());
            file.write(BORDER.getBytes());
        }
    }

    private static void writeCallToFile(Call call, FileOutputStream file) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime startDate = LocalDateTime.parse(call.getStartTime(), formatter);
        LocalDateTime endDate = LocalDateTime.parse(call.getEndTime(), formatter);
        Duration duration = Duration.between(startDate, endDate);
        String formattedDuration = String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart());
        String formattedStartDate = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedEndDate = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String type = call.getType();
        String cost = String.format("%.2f", call.getCost()).replace(",", ".");
        file.write(String.format(
                "|%7s    |%20s |%20s |%9s |%6s |\n",
                type, formattedStartDate, formattedEndDate, formattedDuration, cost
        ).getBytes());
    }

    public static void generateReportsFromFile(File cdrFile) {
        try {
            Scanner scanner = new Scanner(cdrFile);
            Set<Report> reports = new HashSet<>();
            while (scanner.hasNextLine()) {
                String[] record = scanner.nextLine().split(", ");
                String callType = record[0];
                String phoneNumber = record[1];
                String startTime = record[2];
                String endTime = record[3];
                String tariff = record[4];

                Report report = reports.stream()
                        .filter(r -> r.getPhoneNumber().equals(phoneNumber) && r.getTariff().equals(tariff))
                        .findFirst()
                        .orElseGet(() -> {
                            Report newReport = new Report(phoneNumber, tariff);
                            reports.add(newReport);
                            return newReport;
                        });

                Call call = new Call(callType, startTime, endTime, tariff, report.getMinCounter());
                report.getCalls().add(call);
                report.setMinCounter(report.getMinCounter() + call.getCallTime());
            }
            for (Report report : reports) {
                createReportFile(report);
            }

            System.out.println("Done!");
        } catch (IOException e) {
            throw new RuntimeException("File with data not found or problems with generating reports.");
        } catch (ParseException e) {
            throw new RuntimeException("Illegal Data format of Call.");
        }
    }
}
