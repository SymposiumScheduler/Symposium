package symposium;

import symposium.model.Panel;
import symposium.model.ScheduleData;
import symposium.model.TimeFormat;
import symposium.model.TimeRange;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
	    // Reading parsing json files
        //long initTime = System.nanoTime();
        final String INPUT_FILE = "datas/data.txt";
        Parser.parse(INPUT_FILE);
        // Schedule data is initiated

        DummyScheduler bs = new DummyScheduler();
        bs.makeSchedule();
        //long elapsedTime = System.nanoTime() - initTime;

        // Print report
        System.out.print(Report.INSTANCE);
        //System.out.println("\n\n\nTIME= " + (elapsedTime/(double)1000000000) + " ± 0.05 s");
    }

}
