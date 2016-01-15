package symposium;

public class Main {

    public static void main(String[] args) {
	    // Reading parsing json files
        final String INPUT_FILE = "data.txt";
        Parser.initScheduleData(INPUT_FILE);
        // Schedule data is initiated
        DummyScheduler bs = new DummyScheduler();
        bs.makeSchedule();
        // Print report
        System.out.print(Report.INSTANCE);
    }
}
