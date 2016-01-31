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
        System.out.print("\n\n");
        for (String key : Report.errorRecord.keySet()) {
            System.out.println(key + " " + Report.errorRecord.get(key));
        }
    }
}
