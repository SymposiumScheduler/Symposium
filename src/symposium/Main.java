package symposium;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Intercepter;
import com.sun.xml.internal.fastinfoset.sax.SystemIdResolver;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import symposium.model.Panel;
import symposium.model.ScheduleData;
import symposium.model.TimeFormat;
import symposium.model.TimeRange;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
	    // Reading parsing json files

        final String INPUT_FILE = "datas/data.txt";
        Parser.parse(INPUT_FILE);
        // Schedule data is initiated
        long initTime = System.nanoTime();
        DummyScheduler bs = new DummyScheduler();
        bs.makeSchedule();
        long elapsedTime = System.nanoTime() - initTime;

        // Print report
        System.out.print(Report.INSTANCE);
        __debugMinPanelViolations();
        System.out.println("\n\n\nTIME= " + (elapsedTime/(double)1000000000) + " ± 0.05 s");
    }

    private static void __debugMinPanelViolations() {
        System.out.println("\n------------- DEBUG MIN PANELIST VIOLATIONS -----------------");
        int ctr = 0;
        Set<String> checked = new HashSet<>();

        for(Panel p : ScheduleData.instance().getAssignedPanels()) {
            for(String pnst : p.PANELISTS) {
                if(checked.contains(pnst)) {
                    continue;
                }
                //

                pnstDayLoop : for(int day : daysForPanelistAvailability(pnst)) {
                    for(Panel dayP : ScheduleData.instance().getPanelistAssignedPanels(pnst) ){
                        if(dayP.getVenueTime().getDay() == day) {
                            continue pnstDayLoop;
                        }
                    }
                    ctr++;
                    System.out.println(ctr+")"+pnst + " : day " + day);
                }

                //
                checked.add(pnst);
            }
        }
    }
    private static Set<Integer> daysForPanelistAvailability(String pnst) {
        Set<Integer> daysInAvailability = new HashSet<>();
        //assigned
        for(Panel pnstP: ScheduleData.instance().getPanelistAssignedPanels(pnst)){
            Iterator<TimeRange> itr = pnstP.AVAILABILITY.iterator();
            while(itr.hasNext()) {
                daysInAvailability.add(TimeFormat.getNumberOfDay(itr.next().START));
            }
        }
        // un assigned
        for(Panel pnstP: ScheduleData.instance().getFreePanels()){
            if(!pnstP.PANELISTS.contains(pnstP) ){
                continue;
            }
            Iterator<TimeRange> itr = pnstP.AVAILABILITY.iterator();
            while(itr.hasNext()) {
                daysInAvailability.add(TimeFormat.getNumberOfDay(itr.next().START));
            }
        }
        return daysInAvailability;
    }
}
