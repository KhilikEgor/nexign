import services.ReportServices;

import java.io.File;


public class Main {

    public static void main(String[] args) {
        ReportServices.generateReportsFromFile(new File("src/main/resources/data/cdr.txt"));
    }
}

