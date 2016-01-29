package symposium;

import symposium.model.ScheduleData;

public class Main {

    public static void main(String[] args) {
	    // Reading parsing json files
        final String INPUT_FILE = "datas/data.txt";
        Parser.parse(INPUT_FILE);
        // Schedule data is initiated
        DummyScheduler bs = new DummyScheduler();
        bs.makeSchedule();
        // Print report
        System.out.print(Report.INSTANCE);
    }
}
